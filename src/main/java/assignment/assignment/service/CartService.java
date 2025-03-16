package assignment.assignment.service;

import java.util.Collection;
import assignment.assignment.entity.CartItem;

public interface CartService {

    CartItem add(String username, Integer id);

    void remove(String username, Integer productId);

    CartItem update(String username, Integer id, Integer qty);

    void clear(String username);

    Collection<CartItem> getAllItems(String username);

    int getCount(String username);

    double getAmount(String username);

    int getTotalQuantity(String username);
}
