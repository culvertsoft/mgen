import copy
import json

class MGenBase:

    def __init__(self): return
    def __str__(self): return json.dumps(self.__dict__, indent=4, separators=(',', ': '))
    def __hash__(self): return self.toString().__hash__()
    def __eq__(self, other): return self.__class__==other.__class__ and self.__dict__==other.__dict__
    def __ne__(self, other): return not self.__eq__(other)
    
    # Convenience methods
    def toString(self): return self.__str__()
    def equals(self, other): return self == other
    def hashCode(self): return self.__hash__()
    def deepCopy(self): return copy.deepcopy(self)
    
    # Generated instance methods
    def _nFieldsSet(depth, inclTransient): return 0
    def _isFieldSet(field, depth): return False
    def _setAllFieldsSet(self): return
    def _validate(self): return True
    def _accept(self, visitor, selection): return
    def _readField(self, id, context, reader): return False

    # Generated class methods    
    @classmethod
    def _typeId(cls): return -1
    @classmethod
    def _typeIds(cls): return [-1]
    @classmethod
    def _typeId16Bit(cls): return -1
    @classmethod
    def _typeIds16Bit(cls): return [-1]
    @classmethod
    def _typeId16BitBase64(cls): return "xyz"
    @classmethod
    def _typeIds16BitBase64(cls): return ["xyz"]
    @classmethod
    def _typeIds16BitBase64String(cls): return "xyz"
    @classmethod
    def _typeName(cls): return "mgen.classes.MGenBase"
    @classmethod
    def _typeNames(cls): return ["mgen.classes.MGenBase"]
    @classmethod
    def _fields(cls): return []
    @classmethod
    def _fieldById(cls, id): return None
    @classmethod
    def _fieldByName(cls, name): return None
        
#####################################

instance = MGenBase()
x = 1

print("typeId: " + str(instance._typeId()))
print("toString: " + instance.toString())
print("__str__(): " + x.__str__())
print("_typeName(): " + instance._typeName())
print(instance.__hash__())
print(hash(instance))
print(instance.deepCopy())
print(instance.deepCopy().__hash__())
print(id(instance))
print(id(instance.deepCopy()))
print(instance == instance.deepCopy())
print(instance.hashCode() == instance.deepCopy().hashCode())
print(hash(instance) == hash(instance.deepCopy()))
print(id(instance) != id(instance.deepCopy()))
print(instance.equals(instance.deepCopy()))


