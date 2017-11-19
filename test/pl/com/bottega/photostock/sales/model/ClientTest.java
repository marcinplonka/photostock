package pl.com.bottega.photostock.sales.model;

import org.hibernate.service.spi.InjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.photostock.sales.infrastructure.repositories.jparepositories.JPAClientRepository;
import pl.com.bottega.photostock.sales.model.repositories.ClientRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
@SpringBootTest(classes = VIPClient.class)
@RunWith(SpringRunner.class)
public class ClientTest {
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PHOTOSTOCK-TEST");
    EntityManager em = entityManagerFactory.createEntityManager();
    JPAClientRepository clientRepository = new JPAClientRepository(em);

    private Address address = new Address("ul. Północna 11", "Poland", "Lublin", "20-001");
    private Client clientWithCredit = new VIPClient("testWithCredit",
            address,
            ClientStatus.VIP,
            Money.valueOf(100),
            Money.valueOf(100));


    private Client clientWithNoMoney = new VIPClient("testNoMoney", address);

    @Test
    public void shouldCheckIfClientCanAfford() {
        // when
        clientRepository.save(clientWithNoMoney);
        clientWithNoMoney.recharge(Money.valueOf(100));
        clientRepository.save(clientWithNoMoney);

        // then
        Client client = clientRepository.get(clientWithNoMoney.getNumber());
        assertTrue(client.canAfford(Money.valueOf(100)));
        assertTrue(client.canAfford(Money.valueOf(50)));
        assertFalse(client.canAfford(Money.valueOf(101)));
    }

    @Test
    public void shouldCheckIfClientCanAffordWithCredit() {
//        when
        clientRepository.save(clientWithCredit);
        //then
        assertTrue(clientWithCredit.canAfford(Money.valueOf(200)));
        assertFalse(clientWithCredit.canAfford(Money.valueOf(201)));
    }

    @Test
    public void shouldChargeAndRechargeClient() {
        // when
        clientWithCredit.charge(Money.valueOf(200), "Testowy zakup");
        clientWithCredit.recharge(Money.valueOf(100));

        //then
        assertTrue(clientWithCredit.canAfford(Money.valueOf(100)));
        assertFalse(clientWithCredit.canAfford(Money.valueOf(101)));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotAllowToChargeMoreThanCanAfford() {
        clientWithCredit.charge(Money.valueOf(50), "Testowy zakup");
        clientWithCredit.charge(Money.valueOf(100), "Testowy zakup");
        clientWithCredit.charge(Money.valueOf(100), "Testowy zakup");
    }

}
