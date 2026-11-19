// Compile with clang++ hw1.cpp -o hw1
#include <cstddef>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <vector>

int main() {

  //read file into buffer 
  // std::vector<std::byte> res = readFileBytes("listing_0037_single_register_mov");
 
  const auto fileSize = std::filesystem::file_size("listing_0037_single_register_mov");
  std::cout << fileSize << "\n";
  std::ifstream file(filepath, std::ios::binary);
    if (!file) {
        throw std::runtime_error("Failed to open file");
    }

    // 3. Allocate a vector of the exact size and read all bytes at once
    std::vector<std::byte> buffer(fileSize);
    if (!file.read(reinterpret_cast<char*>(buffer.data()), fileSize)) {
        throw std::runtime_error("Failed to read file");
    }


  return 0;
}



//  std::vector<std::byte> readFileBytes(const std::filesystem::path& filepath) {
//     // 1. Get the exact file size (C++17)
//     const auto fileSize = std::filesystem::file_size(filepath);
//
//     // 2. Open the file in binary mode
//     std::ifstream file(filepath, std::ios::binary);
//     if (!file) {
//         throw std::runtime_error("Failed to open file");
//     }
//
//     // 3. Allocate a vector of the exact size and read all bytes at once
//     std::vector<std::byte> buffer(fileSize);
//     if (!file.read(reinterpret_cast<char*>(buffer.data()), fileSize)) {
//         throw std::runtime_error("Failed to read file");
//     }
//
//     return buffer;
// }
