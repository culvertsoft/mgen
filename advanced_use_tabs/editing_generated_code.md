---
---

The MGen compiler lets you edit generated source files like you code regularly. Code generated with MGen contain what we call 'custom code sections'. These are blocks in generated code intended specificly to be edited manually. When you re-generate the source code for your model (e.g. add some fields, rename some members, etc) your custom code sections will be picked up and moved to the newly generated source code file. 

Here is an example. Consider the following class definition for a class called "StreamMsg" ([used by the GPT project for streaming direct3d textures](https://github.com/GiGurra/gpt)):

{% highlight xml %}

<StreamMsg>
  <frameNbr type="int32">-1</frameNbr>
  <width type="int32">0</width>
  <height type="int32">0</height>
  <data type="array[int8]">[]</data>
</StreamMsg>

{% endhighlight %}

Using the MGen compiler to generate C++ code would produce (among other things) a class header starting with:

{% highlight c++ %}

/********************************************************************************************************************
 ********************************************************************************************************************
 ********************************************************************************************************************
           *****                                                                                      *****
           *****               GENERATED WITH MGEN (SNAPSHOT 2014-09-09 23:12:00 +0200)               *****
           *****                                                                                      *****		
 ********************************************************************************************************************
 ********************************************************************************************************************/

#ifndef SE_GIGURRA_GPT_MODEL_DISPLAYS_COMMON_STREAMMSG
#define SE_GIGURRA_GPT_MODEL_DISPLAYS_COMMON_STREAMMSG

#include "mgen/classes/MGenBase.h"
/*custom_includes_begin*//*custom_includes_end*/

namespace se {
namespace gigurra {
namespace gpt {
namespace model {
namespace displays {
namespace common {

class StreamMsg : public mgen::MGenBase /*custom_ifcs_begin*//*custom_ifcs_end*/ {
private:
	int m_frameNbr;
	int m_width;
	int m_height;
	std::vector<char>  m_data;
	bool _m_frameNbr_isSet;
	bool _m_width_isSet;
	bool _m_height_isSet;
	bool _m_data_isSet;

public:
	StreamMsg();
	StreamMsg(const int& frameNbr,
			const int& width,
			const int& height,
			const std::vector<char> & data);
	virtual ~StreamMsg();

	const int& getFrameNbr() const;
	const int& getWidth() const;
	const int& getHeight() const;
	const std::vector<char> & getData() const;

	int& getFrameNbrMutable();
	int& getWidthMutable();
	int& getHeightMutable();
	std::vector<char> & getDataMutable();

	StreamMsg& setFrameNbr(const int& frameNbr);
	StreamMsg& setWidth(const int& width);
	StreamMsg& setHeight(const int& height);
	StreamMsg& setData(const std::vector<char> & data);

	/*custom_methods_begin*//*custom_methods_end*/

	bool hasFrameNbr() const;
	bool hasWidth() const;
	bool hasHeight() const;
	bool hasData() const;

	StreamMsg& unsetFrameNbr();
	StreamMsg& unsetWidth();
	StreamMsg& unsetHeight();
	StreamMsg& unsetData();

	bool operator==(const StreamMsg& other) const;
	bool operator!=(const StreamMsg& other) const;

.
.
.

{% endhighlight %}

As can be seen above, generated c++ classes by default gets 3 generated sections:

 * custom_includes
 * custom_ifcs
 * custom_methods
 
These are names to indicate what you *could* put there - but you can add any valid c++ code.

This functionality is entirely language agnostic and works on string identification level when the MGen compiler is writing generated code to disk - so if you decide to use this feature just make sure to keep generating code to the same place - The MGen compiler will handle the rest.

