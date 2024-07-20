package sg.edu.np.mad.NP_Eats_Team03;

import java.util.List;

public class CustomerOrder {
    public String email;
    public List<Item> orders;

    public CustomerOrder(String email, List<Item> orders) {
        this.email = email;
        this.orders = orders;
    }
}
