package scala.io.mth.pirate.test

object Data {
//
//   implicit def ArbitraryJson: Arbitrary[Json] = {
//    val n = value(jNull)
//    val b = arbitrary[Boolean] map (jBool(_))
//    val m = arbitrary[JsonNumber] map (jNumber(_))
//    val s = arbitrary[String] map (jString(_))
//    val a = for(n <- choose(0, 10);
//                j <- listOfN(n, arbitrary[Json]))
//            yield jArray(j)
//    val o = for(n <- choose(0, 10);
//                j <- listOfN(n, arbitrary[(String, Json)]))
//            yield jObject(j)
//
//    // FIX Would like to pump up the level of complex objects being generated, but it falls over sometimes.
//    Arbitrary(frequency((10, n), (10, b), (10, m), (10, s), (1, a), (4, o)))
//  }
//
//  case class SometimesNullString(s: String) {
//    override def toString = s
//  }
//
//  case class SometimesBoolString(s: String) {
//    override def toString = s
//  }
//
//  implicit val ArbitrarySometimesNullString: Arbitrary[SometimesNullString] =
//      Arbitrary(frequency((1, value("null")), (9, arbitrary[String])) map (SometimesNullString(_)))
//
//  implicit val ArbitrarySometimesBoolString: Arbitrary[SometimesBoolString] =
//      Arbitrary(frequency((1, value("true")), (1, value("false")), (8, arbitrary[String])) map (SometimesBoolString(_)))
//
//  implicit val ArbitraryCannedData: Arbitrary[CannedData] =
//      Arbitrary(oneOf(CannedData.all))
}