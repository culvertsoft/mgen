#include "se/culvertsoft/testmodule/ClassRegistry.cpp"
#include "common.h"

int main() {

    SETUP_WRITERS_AND_READERS(se::culvertsoft::testmodule::ClassRegistry);
    FOR_EACH_SERIALIZER(mkEmptyObjects);
    FOR_EACH_SERIALIZER(mkRandomObjects);

    return 0;

}
