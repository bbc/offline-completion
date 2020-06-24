package steps

import org.scalatest.MustMatchers._
import bbc.cps.OfflineCompletion.reportCompleteness
import cucumber.api.scala.{EN, ScalaDsl}

class CompletionSteps extends ScalaDsl with EN {

  var (predicateCompleteness, numberOfPassports) = ("",0)

  def testReportCompleteness(domain:String, filepath: String) = {
    val result = reportCompleteness(domain, filepath)
    predicateCompleteness = result._1.mkString(",")
    numberOfPassports = result._2
  }

  Given("""passport data has been exported"""){ () =>
  // TODO check file or just move to unit tests
  }

  When("""completion runs for that data""") { () =>
    val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
    val filepath = "/Users/davieg01/Documents/data/newsPassports.txt"
    testReportCompleteness(domain, filepath)
  }

  Then("""^I receive the correct result$""") { () =>

    val expectedPredcates: String =
      "EDITORIAL_TONE -> 6.1,RELEVANT_TO -> 0.0,FORMAT -> 0.0,LANGUAGE -> 100.0,ABOUT -> 48.5," +
        "CONTRIBUTOR -> 0.0,COMMISSIONED_FOR -> 0.0,EDITORIAL_SENSITIVITY -> 33.3,GENRE -> 0.0,SUITABLE_FOR -> 0.0"
    val expectedNumberOfPassports = 33

    predicateCompleteness mustBe expectedPredcates
    numberOfPassports mustBe expectedNumberOfPassports

    predicateCompleteness= _ // to improve
    numberOfPassports = _ // to improve
  }

}
