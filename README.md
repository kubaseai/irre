# irre
Incident Response Readiness Excercises

This repository contains Spring Boot 3 application with specially created vulnerabilities allowing malware persistence via file upload plus file-less in memory attack. The goal of Incident Response Readiness Excercises is to detect 2 attack types.

What did devops do wrong?
1. They provided their Active Directory credentials to modal window of mailcious devops plugin pretending to use credentials to access Internet proxy. It was low throughput exfiltration attack using HTTP GET (not POST) and the connection was attributed to IDE/dev tool, not the plugin.1
2. There is orphaned setting from development stage enabling HTTP Basic Auth using devops:devops credentials. See: https://cwe.mitre.org/data/definitions/276.html
3. BatchId HTTP header is used to override batch identifier which maps 1:1 to folder to store uploaded files, but batchId is not verified so attack using "./../../../" to overwrite OS files is possible. See: https://cwe.mitre.org/data/definitions/20.html, https://cwe.mitre.org/data/definitions/22.html, https://cwe.mitre.org/data/definitions/434.html
4. Deserializer is complex and doesn't follow single responsibility pattern. It's a multi-purpose tool which shouldn't be linked to REST API. As a result uploaded Java bytecode implementing Runnable can executed. It's a design mistake. See: https://cwe.mitre.org/data/definitions/502.html, https://cwe.mitre.org/data/definitions/94.html, 
5. Service Bean contains static object and therefore is very easy target for modification for a malware code without direct access to Java object representing the service. In this example service doing money transfers is attacked - money is transferred to the bank account set by malware (IBAN modified in queued transactions stored in memory).

![Alt text](howto-01.png?raw=true "Howto 01")

![Alt text](howto-02.png?raw=true "Howto 02")

![Alt text](howto-03.png?raw=true "Howto 03")

![Alt text](howto-04.png?raw=true "Howto 04")
