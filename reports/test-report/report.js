$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:src/test/resources/crypto.feature");
formatter.feature({
  "name": "Create Test",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Create Test",
  "description": "",
  "keyword": "Scenario"
});
formatter.step({
  "name": "User running Crypto Script",
  "keyword": "Given "
});
formatter.match({
  "location": "cryptoscript.Crypto.user_running_Crypto_Script()"
});
