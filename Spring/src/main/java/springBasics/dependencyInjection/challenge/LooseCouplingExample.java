package springBasics.dependencyInjection.challenge;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LooseCouplingExample {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationLooseCouplingChallenge.xml");

        UserManager userManagerNewDatabaseProvider = (UserManager) context.getBean("userManagerNewDatabaseProvider");
        System.out.println(userManagerNewDatabaseProvider.getUserInfo());

        UserManager userManagerUserDatabaseProvider = (UserManager) context.getBean("userManagerUserDatabaseProvider");
        System.out.println(userManagerUserDatabaseProvider.getUserInfo());

        UserManager userManagerWebServiceDataProvider = (UserManager) context.getBean("userManagerWebServiceDataProvider");
        System.out.println(userManagerWebServiceDataProvider.getUserInfo());
    }
}
