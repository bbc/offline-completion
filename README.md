# offline-completion

Script to produce a report of passport completion from an exported passports data file.
The script takes two arguments - a domain and filepath.

### Run script example

```bash
sbt run http://www.bbc.co.uk/ontologies/passport/home/News ./data.txt
```
### Run unit tests

```bash
sbt test
```

### Run cucumber tests

```bash
sbt cucumber
```