
#ifndef FLATBUFFERS_HASH_H_
#define FLATBUFFERS_HASH_H_

#include <cstdint>
#include <cstring>

namespace flatbuffers {

template <typename T>
struct FnvTraits {
  static const T kFnvPrime;
  static const T kOffsetBasis;
};

template <>
struct FnvTraits<uint32_t> {
  static const uint32_t kFnvPrime = 0x01000193;
  static const uint32_t kOffsetBasis = 0x811C9DC5;
};

template <>
struct FnvTraits<uint64_t> {
  static const uint64_t kFnvPrime = 0x00000100000001b3;
  static const uint64_t kOffsetBasis = 0xcbf29ce484222645;
};

template <typename T>
T HashFnv1(const char *input) {
  T hash = FnvTraits<T>::kOffsetBasis;
  for (const char *c = input; *c; ++c) {
    hash *= FnvTraits<T>::kFnvPrime;
    hash ^= static_cast<unsigned char>(*c);
  }
  return hash;
}

template <typename T>
T HashFnv1a(const char *input) {
  T hash = FnvTraits<T>::kOffsetBasis;
  for (const char *c = input; *c; ++c) {
    hash ^= static_cast<unsigned char>(*c);
    hash *= FnvTraits<T>::kFnvPrime;
  }
  return hash;
}

template <typename T>
struct NamedHashFunction {
  const char *name;

  typedef T (*HashFunction)(const char*);
  HashFunction function;
};

const NamedHashFunction<uint32_t> kHashFunctions32[] = {
  { "fnv1_32",  HashFnv1<uint32_t> },
  { "fnv1a_32", HashFnv1a<uint32_t> },
};

const NamedHashFunction<uint64_t> kHashFunctions64[] = {
  { "fnv1_64",  HashFnv1<uint64_t> },
  { "fnv1a_64", HashFnv1a<uint64_t> },
};

inline NamedHashFunction<uint32_t>::HashFunction FindHashFunction32(
    const char *name) {
  std::size_t size = sizeof(kHashFunctions32) / sizeof(kHashFunctions32[0]);
  for (std::size_t i = 0; i < size; ++i) {
    if (std::strcmp(name, kHashFunctions32[i].name) == 0) {
      return kHashFunctions32[i].function;
    }
  }
  return nullptr;
}

inline NamedHashFunction<uint64_t>::HashFunction FindHashFunction64(
    const char *name) {
  std::size_t size = sizeof(kHashFunctions64) / sizeof(kHashFunctions64[0]);
  for (std::size_t i = 0; i < size; ++i) {
    if (std::strcmp(name, kHashFunctions64[i].name) == 0) {
      return kHashFunctions64[i].function;
    }
  }
  return nullptr;
}

}  // namespace flatbuffers

#endif  // FLATBUFFERS_HASH_H_
