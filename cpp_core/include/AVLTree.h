#ifndef AVL_TREE_H
#define AVL_TREE_H

#include <string>
#include <vector>
#include <utility>

/**
 * AVL Tree implementation for sorted vendor data
 */
class AVLTree {
public:
    AVLTree();
    ~AVLTree();

    // Insert key-value pair (key = vendorId, value = score)
    void insert(const std::string& key, double value);

    // Get value by key
    double get(const std::string& key);

    // Check if key exists
    bool contains(const std::string& key);

    // Get all key-value pairs in sorted order (ascending)
    std::vector<std::pair<std::string, double>> getAllSorted();

    // Get all key-value pairs in reverse sorted order (descending)
    std::vector<std::pair<std::string, double>> getAllSortedReverse();

    // Get size
    int size();

private:
    struct AVLNode {
        std::string key;
        double value;
        AVLNode* left;
        AVLNode* right;
        int height;

        AVLNode(const std::string& k, double v) 
            : key(k), value(v), left(nullptr), right(nullptr), height(1) {}
    };

    AVLNode* root;
    int treeSize;

    // AVL tree operations
    int getHeight(AVLNode* node);
    int getBalance(AVLNode* node);
    AVLNode* rightRotate(AVLNode* y);
    AVLNode* leftRotate(AVLNode* x);
    AVLNode* insertHelper(AVLNode* node, const std::string& key, double value);
    AVLNode* searchHelper(AVLNode* node, const std::string& key);
    void inOrderTraversal(AVLNode* node, std::vector<std::pair<std::string, double>>& result);
    void inOrderTraversalReverse(AVLNode* node, std::vector<std::pair<std::string, double>>& result);
    void deleteTree(AVLNode* node);
};

#endif // AVL_TREE_H

