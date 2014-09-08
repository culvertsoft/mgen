---
---

Using Maven (or ivy/gradle/sbt or similar) is probably the easiest way is to depend on MGen for java builds. Currently only snapshot builds are available. All you need to do is att 'mgen-javalib' as a dependency:

    <dependency>
      <groupId>se.culvertsoft</groupId>
      <artifactId>mgen-javalib</artifactId>
      <version>SNAPSHOT</version>
    </dependency>

They are hosted on the following repository urls:
 
 * https://oss.sonatype.org/content/repositories/snapshots/
 * https://oss.sonatype.org/content/repositories/releases/

To see all the MGen components hosted there - check out [oss.sonatype.org/#nexus-search;quick~mgen](https://oss.sonatype.org/#nexus-search;quick~mgen)

