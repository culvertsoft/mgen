#include "se/culvertsoft/ClassRegistry.cpp"
#include "common.h"

using namespace se::culvertsoft;
using namespace se::culvertsoft::basemodule;
using namespace se::culvertsoft::depmodule;

int main() {

    MK_MGEN_OUTPUT(ClassRegistry, mgen::JsonWriter, true);
    MK_MGEN_INPUT(ClassRegistry, mgen::JsonReader);

    return 0;
}
