#include "se/culvertsoft/ClassRegistry.cpp"
#include "MGenObjectRandomizer.h"
#include "common.h"

#include <iostream>

template<typename ClassRegType, typename OutStreamType, typename InStreamType, typename WriterType, typename ReaderType>
static void mkEmptyObjects(
        const ClassRegType& classRegistry,
        std::vector<char>& buffer,
        OutStreamType& outputStream,
        InStreamType& inputStream,
        const std::string& serializerName,
        WriterType& writer,
        ReaderType& reader,
        const bool isCompact) {

    const typename ClassRegType::EntryMap& entries = classRegistry.entries();

    for (typename ClassRegType::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * instance = it->second.newInstance();
        instance->_setAllFieldsSet(true, mgen::DEEP);
        writer.writeObject(*instance);
        delete instance;
    }

    writeToFile(std::string("../data_generated/emptyObjects_").append(serializerName).append(".data"), buffer);

    buffer.clear();

}

template<typename ClassRegType, typename OutStreamType, typename InStreamType, typename WriterType, typename ReaderType>
static void mkRandomObjects(
        const ClassRegType& classRegistry,
        std::vector<char>& buffer,
        OutStreamType& outputStream,
        InStreamType& inputStream,
        const std::string& serializerName,
        WriterType& writer,
        ReaderType& reader,
        const bool isCompact) {

    mgen::ObjectRandomizer<ClassRegType> randomizer(classRegistry);

    const typename ClassRegType::EntryMap& entries = classRegistry.entries();

    for (typename ClassRegType::EntryMap::const_iterator it = entries.begin(); it != entries.end(); it++) {
        mgen::MGenBase * instance = it->second.newInstance();
        instance->_setAllFieldsSet(true, mgen::DEEP);
        randomizer.randomizeObject(*instance);
        writer.writeObject(*instance);
        delete instance;
    }

    writeToFile(std::string("../data_generated/randomizedObjects_").append(serializerName).append(".data"), buffer);

    buffer.clear();

}

int main() {

    SETUP_WRITERS_AND_READERS(se::culvertsoft::ClassRegistry);
    FOR_EACH_SERIALIZER(mkEmptyObjects);
    FOR_EACH_SERIALIZER(mkRandomObjects);

    return 0;

}
