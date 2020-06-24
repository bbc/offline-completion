package bbc.cps

import java.io.FileNotFoundException

import org.scalatest.{FlatSpec, MustMatchers}

class OfflineCompletionTest  extends FlatSpec with MustMatchers {

    "reportCompleteness()" should "handle a missing data file" in {
      val filepath = "/somedir/passport.txt"
      val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"

      intercept[FileNotFoundException] {
       OfflineCompletion.reportCompleteness(domain, filepath)
      }.getMessage must include (s"Could not find file $filepath")
    }

    it should "handle no data in file" in {
      val filepath = getClass.getResource("/no-passports.txt").getPath()
      val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
      val result = OfflineCompletion.reportCompleteness(domain, filepath)
      result._1.isEmpty mustBe true
      result._2 mustBe 0
    }

    it should "report News completeness correctly from data containing News passports containing no locators" in {
      val filepath = getClass.getResource("/news-no-locators.txt").getPath()
      val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
      val result = OfflineCompletion.reportCompleteness(domain, filepath)
      result._1.isEmpty mustBe true
      result._2 mustBe 0
    }

    it should "report News completeness correctly from data containing News passports containing only user locators" in {
      val filepath = getClass.getResource("/news-user-locators.txt").getPath()
      val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
      val result = OfflineCompletion.reportCompleteness(domain, filepath)
      result._1.isEmpty mustBe true
      result._2 mustBe 0
    }

    it should "report News completeness from data containing News passports containing no taggings" in {
      val filepath = getClass.getResource("/news-no-taggings.txt").getPath()
      val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
      val result = OfflineCompletion.reportCompleteness(domain, filepath)
      result._1("LANGUAGE") mustBe "100.0"
      result._2 mustBe 9
    }

    it should "report News completeness from data containing News passports containing no taggings and no language predicate" in {
      val filepath = getClass.getResource("/news-no-taggings-no-language.txt").getPath()
      val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
      val result = OfflineCompletion.reportCompleteness(domain, filepath)
      result._1("LANGUAGE") mustBe "0.0"
      result._2 mustBe 9
    }

    it should "report News completeness from data containing passports but no News passports" in {
      val filepath = getClass.getResource("/no-news-passports.txt").getPath()
      val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
      val result = OfflineCompletion.reportCompleteness(domain, filepath)
      result._1.isEmpty mustBe true
      result._2 mustBe 0
    }

  it should "report News completeness from data" in {
    val filepath = getClass.getResource("/passports.txt").getPath()
    val domain = "http://www.bbc.co.uk/ontologies/passport/home/News"
    val result = OfflineCompletion.reportCompleteness(domain, filepath)

    val expected = Map(
      "EDITORIAL_TONE" -> "33.3",
      "RELEVANT_TO" -> "26.7",
      "FORMAT" -> "26.7",
      "LANGUAGE" -> "100.0",
      "ABOUT" -> "73.3",
      "CONTRIBUTOR" -> "26.7",
      "COMMISSIONED_FOR" -> "13.3",
      "EDITORIAL_SENSITIVITY" -> "20.0",
      "GENRE" -> "26.7",
      "SUITABLE_FOR" -> "0.0")

    result._1 mustBe  expected
    result._2 mustBe 15
  }
}
