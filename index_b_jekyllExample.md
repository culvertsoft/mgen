---
layout: default
link-title: Jekyll Example
submenu:
  - { anchor: "one", title: "anchor1" }
  - { anchor: "two", title: "anchor2" }
--- 

#JavaScript

Work in progress to test jekyll.

### Anchor 1 <a name="one">&nbsp;</a>

{% highlight javascript %}

var a = "Some Javascript";

var b = (function(a, b){
  return a + b;
}(1, 2));

{% endhighlight %}

{% highlight cpp %}

// First we create a class registry.
ClassRegistry classRegistry;

// We will serialize our objects to this std::vector of bytes
std::vector<char> buffer;

// Create an OutputStream object around the vector (MGen readers and writers accept only streams)
VectorOutputStream out(buffer);

// Now create our serializer
JsonWriter<VectorOutputStream, ClassRegistry> writer(out, classRegistry);

// Write the objects
writer.writeObject(car1);
writer.writeObject(car2);
writer.writeObject(car3);

{% endhighlight %}


java
{% highlight java %}

public static void main(String[] args){
  if(args.length > 0){
    Car car = new Car(args);
    car.run();
  }
  return 0;
}

{% endhighlight %}


xml
{% highlight xml %}

<Module>
  <Types>

    <Apple>
      <size type="int32"/>
      <brand type="string"/>
    </Apple>

    <Store>
      <stock type="list[Apple]"/>
      <price type="int32"/>
      <name type="string"/>
    </Store>

  </Types>
</Module>

{% endhighlight %}


### Anchor 2<a name="two">&nbsp;</a>