---
---

Using Maven (or ivy/gradle/sbt or similar) is probably the easiest way is to depend on MGen for java builds. Just add 'mgen-javalib' as a dependency to your application:


For the current release (beta 0.1) :

{% highlight xml %}

<dependency>
  <groupId>se.culvertsoft</groupId>
  <artifactId>mgen-javalib</artifactId>
  <version>0.1</version>
</dependency>

{% endhighlight %}


For snapshots:

{% highlight xml %}

<dependency>
  <groupId>se.culvertsoft</groupId>
  <artifactId>mgen-javalib</artifactId>
  <version>SNAPSHOT</version>
</dependency>

{% endhighlight %}

They are hosted on the following repository urls:
 
 * https://oss.sonatype.org/content/repositories/snapshots/
 * https://oss.sonatype.org/content/repositories/releases/

To see all the MGen components hosted there - check out [oss.sonatype.org/#nexus-search;quick~mgen](https://oss.sonatype.org/#nexus-search;quick~mgen)

