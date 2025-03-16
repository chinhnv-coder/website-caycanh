package assignment.assignment.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import assignment.assignment.DAO.ProductDAO;
import assignment.assignment.entity.CartItem;
import assignment.assignment.entity.Product;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductDAO productDAO;

    // Lưu giỏ hàng theo tài khoản
    private Map<String, Map<Integer, CartItem>> userCarts = new HashMap<>();

    private Map<Integer, CartItem> getUserCart(String username) {
        return userCarts.computeIfAbsent(username, k -> new HashMap<>());
    }

    @Override
    public CartItem add(String username, Integer id) {
        Map<Integer, CartItem> cart = getUserCart(username);
        CartItem item = cart.get(id);
        if (item == null) {
            Product product = productDAO.findById(id).orElse(null);
            if (product != null) {
                item = new CartItem(product, 1, username);
                cart.put(id, item);
            }
        } else {
            item.setQty(item.getQty() + 1);
        }
        return item;
    }

    @Override
    public void remove(String username, Integer id) {
        getUserCart(username).remove(id);
    }

    @Override
    public CartItem update(String username, Integer id, Integer qty) {
        Map<Integer, CartItem> cart = getUserCart(username);
        CartItem item = cart.get(id);
        if (item != null) {
            item.setQty(qty);
        }
        return item;
    }

    @Override
    public void clear(String username) {
        getUserCart(username).clear();
    }

    @Override
    public Collection<CartItem> getAllItems(String username) {
        return getUserCart(username).values();
    }

    @Override
    public int getCount(String username) {
        return getUserCart(username).size();
    }

    @Override
    public double getAmount(String username) {
        return getUserCart(username).values().stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    @Override
    public int getTotalQuantity(String username) {
        return getUserCart(username).values().stream().mapToInt(CartItem::getQty).sum();
    }
}
