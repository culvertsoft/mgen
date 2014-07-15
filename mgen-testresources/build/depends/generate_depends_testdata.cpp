#include "se/culvertsoft/ClassRegistry.cpp"
#include "common.h"

#include <iostream>

using namespace se::culvertsoft;
using namespace se::culvertsoft::basemodule;
using namespace se::culvertsoft::depmodule;

template<typename ClassRegType, typename OutStreamType, typename InStreamType, typename WriterType, typename ReaderType>
static void ftest(
        const ClassRegType& classRegistry,
        std::vector<char>& buffer,
        OutStreamType& outputStream,
        InStreamType& inputStream,
        const std::string& serializerName,
        WriterType& writer,
        ReaderType& reader,
        const bool isCompact) {

    typedef ClassRegistry::EntryMap::const_iterator TIt;
    const ClassRegistry::EntryMap& entries = classRegistry.entries();

    for (TIt it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * instance = it->second.newInstance();
        instance->_setAllFieldsSet(true, mgen::DEEP);
        writer.writeObject(*instance);
        delete instance;
    }

    writeToFile(std::string("../data_generated/").append(serializerName).append(".data"), buffer);

    buffer.clear();

}

int main() {

    SETUP_WRITERS_AND_READERS(ClassRegistry);
    FOR_EACH_SERIALIZER(ftest);

    return 0;

}
