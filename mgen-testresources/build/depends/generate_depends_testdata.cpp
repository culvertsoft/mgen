
#include "se/culvertsoft/ClassRegistry.cpp"

#include "common.h"

#include <iostream>

int main() {

    MK_MGEN_OUTPUT(se::culvertsoft::ClassRegistry, mgen::JsonWriter, true)

    return 0;
}
