package bbc.cps

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse

import scala.io.Source

object OfflineCompletion {
  implicit val formats = DefaultFormats

  val predicateUrl = Map(
    "EDITORIAL_SENSITIVITY" -> "http://www.bbc.co.uk/ontologies/coreconcepts/editorialSensitivity",
    "ABOUT" -> "http://www.bbc.co.uk/ontologies/passport/predicate/About",
    "EDITORIAL_TONE" -> "http://www.bbc.co.uk/ontologies/coreconcepts/editorialTone",
    "RELEVANT_TO" -> "http://www.bbc.co.uk/ontologies/bbc/relevantTo",
    "EDITORIAL_TONE" -> "http://www.bbc.co.uk/ontologies/coreconcepts/editorialTone",
    "GENRE" -> "http://www.bbc.co.uk/ontologies/bbc/genre",
    "LANGUAGE" -> "http://www.bbc.co.uk/ontologies/coreconcepts/language",
    "FORMAT" -> "http://www.bbc.co.uk/ontologies/cwork/format",
    "CONTRIBUTOR" -> "http://www.bbc.co.uk/ontologies/bbc/contributor",
    "COMMISSIONED_FOR" -> "http://www.bbc.co.uk/ontologies/bbc/commissionedFor",
    "SUITABLE_FOR" -> "http://www.bbc.co.uk/ontologies/bbc/suitableFor")

  case class SummaryCount(predicateCount: Map[String, Int], numberOfPassports: Int)

  case class Predicate(pname:String, url:String)

  private def passportContainsPredicate(passport: Passport, predicate: Predicate) = predicate match {
    case Predicate("LANGUAGE", url) => (url, passport.language.isDefined)
    case Predicate(_, url) => passport.taggings match {
      case Some(taggings) => (url, taggings.map(_.predicate).contains(url))
      case None => (url, false)
    }
  }

  private def generatePredicateMapForPassport(passport: Passport) =
    predicateUrl.foldLeft(Map.empty[String, Boolean]) { (accum, predicateMapping) =>
      accum + { passportContainsPredicate(passport, Predicate.tupled(predicateMapping)) }
    }

  private def excludePassport(p:Passport) = p.locator match {
    case Some(locator) => locator.contains("urn:bbc:cps:user:")
    case None => true
  }

  private def generatePassportSummary(filePath: String, domain: String) = {
    val summary = Source.fromFile(filePath).getLines
      .toList
      .map(parse(_).extract[Passport])
      .filterNot(excludePassport(_))
      .filter(_.home.contains(domain))
      .zipWithIndex
      .foldLeft((Map.empty[String, Int], 0)) {
        (accum, passport) => {
          (accum._1 ++ generatePredicateMapForPassport(passport._1).map {
              case (k, v) => k -> ((if (v) 1 else 0) + accum._1.getOrElse(k, 0))
            },
            passport._2 + 1
          )
        }
      }

    SummaryCount.tupled(summary)
  }

  private def calculatePercentage(x:Int, y:Int) = (x.toDouble / y.toDouble) * 100

  private def formatResult(x: Double) = {f"$x%1.1f"}

  private def makeResults(summary: SummaryCount): Map[String, Double] =
     summary.predicateCount.transform((_, pCount) => calculatePercentage(pCount, summary.numberOfPassports))

  private def tidyResults(results: Map[String, Double]): Map[String, String] =
    results.map { case (key, value) => predicateUrl.find(_._2 == key).get._1 -> formatResult(value) }

  def reportCompleteness(domain: String, filePath: String): (Map[String, String], Int) = {
    val predicateSummary = generatePassportSummary(filePath, domain)
    (tidyResults(makeResults(predicateSummary)), predicateSummary.numberOfPassports)
  }

  def main(args: Array[String]): Unit = {
    val (domain, filePath) = (args(0), args(1))
    val (passportCompleteness, numberOfPassports) = reportCompleteness(domain, filePath)

    // Display formatted table of completeness results.
    if (passportCompleteness.isEmpty) {
      println(s"\nNo passports found where passport home = $domain")
    }
    else {
      println(s"\nCompleteness for $domain")
      println(s"Number of passports analyzed:  $numberOfPassports")
      println(Tabulator.format(List("Predicate", "Completeness") :: passportCompleteness.map(x => List(x._1, x._2+"%")).toList))
    }
  }
}