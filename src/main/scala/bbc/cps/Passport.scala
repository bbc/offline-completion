package bbc.cps

case class _id(`$oid`: String
              )

case class Tagging(predicate: String,
                    value: String
                   )

case class Passport(_id: _id,
                    passportId: Option[String],
                    language: Option[String],
                    locator: Option[String] = None,
                    home: Option[String] = None,
                    genre: Option[String] = None,
                    taggings: Option[List[Tagging]] = None
                   )