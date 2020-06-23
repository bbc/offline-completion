package steps

import bbc.cps.OfflineCompletion.completeness
import cucumber.api.scala.{EN, ScalaDsl}

class mainSteps extends ScalaDsl with EN {

  Given("""completion runs for matching home and no locators"""){ () =>
    val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
    val filepath = "/Users/davieg01/Documents/data/homeNewsNoLocators.json"
    val result = completeness(domain, filepath)
    println(result)
  }

  Given("""completion runs for Test"""){ () =>
    val domain = "http://www.bbc.co.uk/ontologies/passport/home/BBCThree"
    val filepath = "/Users/davieg01/Documents/data/passportsTest.json"
    val result = completeness(domain, filepath)
    println(result)
  }

  Given("""completion runs for Live"""){ () =>
    val domain = "http://www.bbc.co.uk/ontologies/passport/home/BBCThree"
    val filepath = "/Users/davieg01/Documents/data/passportsLive.json"
    val result = completeness(domain, filepath)
    println(result)
  }
}
