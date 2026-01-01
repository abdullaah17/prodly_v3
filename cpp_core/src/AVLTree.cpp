#include "../include/AVLTree.h"
#include <algorithm>
#include <cmath>

AVLTree::AVLTree() : root(nullptr), treeSize(0) {
}

AVLTree::~AVLTree() {
    deleteTree(root);
}

void AVLTree::deleteTree(AVLNode* node) {
    if (node != nullptr) {
        deleteTree(node->left);
        deleteTree(node->right);
        delete node;
    }
}

int AVLTree::getHeight(AVLNode* node) {
    if (node == nullptr) return 0;
    return node->height;
}

int AVLTree::getBalance(AVLNode* node) {
    if (node == nullptr) return 0;
    return getHeight(node->left) - getHeight(node->right);
}

AVLTree::AVLNode* AVLTree::rightRotate(AVLNode* y) {
    AVLNode* x = y->left;
    AVLNode* T2 = x->right;
    
    x->right = y;
    y->left = T2;
    
    y->height = std::max(getHeight(y->left), getHeight(y->right)) + 1;
    x->height = std::max(getHeight(x->left), getHeight(x->right)) + 1;
    
    return x;
}

AVLTree::AVLNode* AVLTree::leftRotate(AVLNode* x) {
    AVLNode* y = x->right;
    AVLNode* T2 = y->left;
    
    y->left = x;
    x->right = T2;
    
    x->height = std::max(getHeight(x->left), getHeight(x->right)) + 1;
    y->height = std::max(getHeight(y->left), getHeight(y->right)) + 1;
    
    return y;
}

AVLTree::AVLNode* AVLTree::insertHelper(AVLNode* node, const std::string& key, double value) {
    if (node == nullptr) {
        treeSize++;
        return new AVLNode(key, value);
    }
    
    if (key < node->key) {
        node->left = insertHelper(node->left, key, value);
    } else if (key > node->key) {
        node->right = insertHelper(node->right, key, value);
    } else {
        // Key already exists, update value
        node->value = value;
        return node;
    }
    
    node->height = std::max(getHeight(node->left), getHeight(node->right)) + 1;
    
    int balance = getBalance(node);
    
    // Left Left Case
    if (balance > 1 && key < node->left->key) {
        return rightRotate(node);
    }
    
    // Right Right Case
    if (balance < -1 && key > node->right->key) {
        return leftRotate(node);
    }
    
    // Left Right Case
    if (balance > 1 && key > node->left->key) {
        node->left = leftRotate(node->left);
        return rightRotate(node);
    }
    
    // Right Left Case
    if (balance < -1 && key < node->right->key) {
        node->right = rightRotate(node->right);
        return leftRotate(node);
    }
    
    return node;
}

void AVLTree::insert(const std::string& key, double value) {
    root = insertHelper(root, key, value);
}

AVLTree::AVLNode* AVLTree::searchHelper(AVLNode* node, const std::string& key) {
    if (node == nullptr || node->key == key) {
        return node;
    }
    
    if (key < node->key) {
        return searchHelper(node->left, key);
    }
    
    return searchHelper(node->right, key);
}

double AVLTree::get(const std::string& key) {
    AVLNode* node = searchHelper(root, key);
    if (node != nullptr) {
        return node->value;
    }
    return 0.0;
}

bool AVLTree::contains(const std::string& key) {
    return searchHelper(root, key) != nullptr;
}

void AVLTree::inOrderTraversal(AVLNode* node, std::vector<std::pair<std::string, double>>& result) {
    if (node != nullptr) {
        inOrderTraversal(node->left, result);
        result.push_back(std::make_pair(node->key, node->value));
        inOrderTraversal(node->right, result);
    }
}

void AVLTree::inOrderTraversalReverse(AVLNode* node, std::vector<std::pair<std::string, double>>& result) {
    if (node != nullptr) {
        inOrderTraversalReverse(node->right, result);
        result.push_back(std::make_pair(node->key, node->value));
        inOrderTraversalReverse(node->left, result);
    }
}

std::vector<std::pair<std::string, double>> AVLTree::getAllSorted() {
    std::vector<std::pair<std::string, double>> result;
    inOrderTraversal(root, result);
    return result;
}

std::vector<std::pair<std::string, double>> AVLTree::getAllSortedReverse() {
    std::vector<std::pair<std::string, double>> result;
    inOrderTraversalReverse(root, result);
    return result;
}

int AVLTree::size() {
    return treeSize;
}

