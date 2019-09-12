import controller.ContactController;
import model.Contact;
import model.Group;
import model.Phone;

import java.lang.management.GarbageCollectorMXBean;

public class Main {
    public static void main(String[] args) {
        Phone p1 = new Phone("123456");
        Phone p2 = new Phone("123456");
        Phone p3 = new Phone("123456");
        Phone p4 = new Phone("123456");

        Group g1 = new Group("teste 1");
        Group g2 = new Group("teste 2");

        Contact c = new Contact("Teste", "2", "teste2@teste.com");
        c.addPhone(p1);
        c.addPhone(p2);
        c.addPhone(p3);
        c.addPhone(p4);

        c.addGroup(g1);
        c.addGroup(g2);

        System.out.println(new ContactController().create(c));
    }

}
