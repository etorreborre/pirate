package pirate.spec

import pirate._
import pirate.internal._
import scalaz.{ListT => _, Name => _, _}, Scalaz._
import org.scalacheck.{Arbitrary, Gen}, Arbitrary._

object Arbitraries {
  case class SmallInt(value: Int)

  implicit def SmallIntArbitrary: Arbitrary[SmallInt] =
    Arbitrary(Gen.choose(0, 1000) map SmallInt.apply)

  case class LongLine(value: String)

  case class List5[A](value: List[A])
  implicit def List5Arbitrary[A: Arbitrary]: Arbitrary[List5[A]] =
    Arbitrary(for {
      n <- Gen.choose(0, 5)
      l <- Gen.listOfN(n, arbitrary[A])
    } yield List5(l))

  implicit def LongLineArbitrary: Arbitrary[LongLine] =
    Arbitrary(Gen.resize(1000, arbitrary[String]) map LongLine.apply)

  implicit def NameArbitrary: Arbitrary[Name] =
    Arbitrary(Gen.oneOf(
      Gen.alphaChar map ShortName
    , Gen.alphaStr map LongName
    , for { c <- Gen.alphaChar; s <- Gen.alphaStr } yield Name(c, s)
    ))

  implicit def TStepArbitrary[A: Arbitrary, X: Arbitrary]: Arbitrary[TStep[A, X]] =
    Arbitrary(Gen.oneOf(
      Gen.oneOf(Seq(TNil[A, X]()))
    , for { a <- arbitrary[A]; x <- arbitrary[X] } yield TCons(a, x)
    ))

  implicit def ListTArbitrary[A: Arbitrary]: Arbitrary[ListT[Identity, A]] =
    Arbitrary(for {
      n <- Gen.choose(0, 10)
      x <- Gen.listOfN(n, arbitrary[A])
    } yield x.foldRight(ListT.nil[Identity, A])(ListT.cons[Identity, A]))

  implicit def NondetTArbitrary[A: Arbitrary]: Arbitrary[NondetT[Identity, A]] = {
    type S[B] = StateT[Identity, Boolean, B]
    Arbitrary(for {
      n <- Gen.choose(0, 10)
      x <- Gen.listOfN(n, arbitrary[A])
    } yield NondetT(x.foldRight(ListT.nil[S, A])(ListT.cons[S, A])))
  }

  implicit def ParseArbitrary[A: Read: Arbitrary]: Arbitrary[Parse[A]] =
    Arbitrary(
      Gen.lzy(
        Gen.oneOf(
          arbitrary[A].map(a => ValueParse(a))
        , arbitrary[Parser[A]].map(p => ParserParse(p))
        , for {
            a <- arbitrary[Parse[A]]
            b <- arbitrary[Parse[A]]
          } yield(AltParse(a, b))
        )
      )
    )

  implicit def ParserArbitrary[A: Read: Arbitrary]: Arbitrary[Parser[A]] =
    Arbitrary(
      Gen.oneOf(
        arbitrary[SwitchParser[A]]
      , arbitrary[FlagParser[A]]
      , arbitrary[ArgumentParser[A]]
    ))

  implicit def SwitchParserArbitrary[A: Read: Arbitrary]: Arbitrary[SwitchParser[A]] = Arbitrary(for {
    n <- arbitrary[Name]
    desc <- arbitrary[Option[String]]
    mvar <- arbitrary[Option[String]]
    hid <- arbitrary[Boolean]
    a <- arbitrary[A]
  } yield SwitchParser[A](n, Metadata(desc, mvar, hid), a))

  implicit def FlagParserArbitrary[A: Read: Arbitrary]: Arbitrary[FlagParser[A]] = Arbitrary(for {
    n <- arbitrary[Name]
    desc <- arbitrary[Option[String]]
    mvar <- arbitrary[Option[String]]
    hid <- arbitrary[Boolean]
  } yield FlagParser[A](n, Metadata(desc, mvar, hid), Read.of[A]))

  implicit def ArgumentParserArbitrary[A: Read: Arbitrary]: Arbitrary[ArgumentParser[A]] = Arbitrary(for {
    desc <- arbitrary[Option[String]]
    mvar <- arbitrary[Option[String]]
    hid <- arbitrary[Boolean]
  } yield ArgumentParser[A](Metadata(desc, mvar, hid), Read.of[A]))
}
