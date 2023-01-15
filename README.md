# irre
Incident Response Readiness Excercises

This repository contains Spring Boot 3 application with specially created vulnerabilities allowing malware persistence via file upload plus file-less in memory attack. The goal of Incident Response Readiness Excercises is to detect 2 attack types.

What devops did wrong?
1. There is orphaned settings from development stage enabling HTTP Basic Auth using devops:devops credentials.
2. BatchId HTTP header is used to override batch identifier which maps 1:1 to folder to store uploaded files, but batchId is not verified so attack using "./../../../" to overwrite OS files is possible.
3. Deserializer is complex and doesn't follow single responsibility pattern. It's a multi-purpose tool which shouldn't be linked to REST API. As a result uploaded Java bytecode implementing Runnable can executed. It's a design mistake.
4. Service Bean contains static object and therefore is very easy target for modification for a malware code without direct access to Java object representing the service.

![Alt text](howto-01.png?raw=true "Howto 01")

![Alt text](howto-02.png?raw=true "Howto 02")

![Alt text](howto-03.png?raw=true "Howto 03")

![Alt text](howto-04.png?raw=true "Howto 04")
