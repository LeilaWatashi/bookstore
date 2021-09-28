package com.watashi.bookstore.facade;

import com.watashi.bookstore.dao.*;
import com.watashi.bookstore.entity.AssociativeDomainEntity;
import com.watashi.bookstore.entity.Entity;
import com.watashi.bookstore.entity.product.Category;
import com.watashi.bookstore.entity.product.Product;
import com.watashi.bookstore.entity.shop.*;
import com.watashi.bookstore.entity.stock.Stock;
import com.watashi.bookstore.entity.stock.StockHistory;
import com.watashi.bookstore.entity.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Facade<T extends Entity> implements IFacade<T> {

    private final Map<String, DAO> daosMap;

    private CustomerDAO customerDAO;
    private AddressDAO addressDAO;
    private CreditCardDAO creditCardDAO;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    private StockDAO stockDAO;
    private StockHistoryDAO stockHistoryDAO;

    private VoucherDAO voucherDAO;
    private UserVoucherDAO customerVoucherDAO;

    private SaleDAO saleDAO;
    private SaleItemDAO saleItemDAO;
    private SaleAddressDAO saleAddressDAO;
    private SaleCreditCardDAO saleCreditCardDAO;

    private TradeDAO tradeDAO;
    private TradeItemDAO tradeItemDAO;

    public Facade() {
        daosMap = new HashMap<>();
    }

    @Autowired
    private void setCustomerRelatedDAOS(CustomerDAO customerDAO,
                                        AddressDAO addressDAO,
                                        CreditCardDAO creditCardDAO) {
        this.customerDAO = customerDAO;
        this.addressDAO = addressDAO;
        this.creditCardDAO = creditCardDAO;

        initCustomerRelatedDAOSMap();
    }

    @Autowired
    private void setProductRelatedDAOS(ProductDAO productDAO,
                                       CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;

        initProductRelatedDAOSMap();
    }

    @Autowired
    private void setStockRelatedDAOS(StockDAO stockDAO,
                                     StockHistoryDAO stockHistoryDAO) {
        this.stockDAO = stockDAO;
        this.stockHistoryDAO = stockHistoryDAO;

        initStockRelatedDAOSMap();
    }

    @Autowired
    private void setSaleRelatedDAOS(SaleDAO saleDAO,
                                    SaleItemDAO saleItemDAO,
                                    SaleAddressDAO saleAddressDAO,
                                    SaleCreditCardDAO saleCreditCardDAO) {
        this.saleDAO = saleDAO;
        this.saleItemDAO = saleItemDAO;
        this.saleAddressDAO = saleAddressDAO;
        this.saleCreditCardDAO = saleCreditCardDAO;

        initSaleRelatedDAOSMap();
    }

    @Autowired
    private void setVoucherRelatedDAOS(VoucherDAO voucherDAO,
                                      UserVoucherDAO customerVoucherDAO) {
        this.voucherDAO = voucherDAO;
        this.customerVoucherDAO = customerVoucherDAO;

        initVoucherRelatedDAOSMap();
    }

    @Autowired
    private void setTradeRelatedDAOS(TradeDAO tradeDAO,
                                     TradeItemDAO tradeItemDAO) {
        this.tradeDAO = tradeDAO;
        this.tradeItemDAO = tradeItemDAO;

        initTradeRelatedDAOSMap();
    }

    private void initCustomerRelatedDAOSMap() {
        daosMap.put(Customer.class.getName(), customerDAO);
        daosMap.put(Address.class.getName(), addressDAO);
        daosMap.put(CreditCard.class.getName(), creditCardDAO);
    }

    private void initProductRelatedDAOSMap() {
        daosMap.put(Product.class.getName(), productDAO);
        daosMap.put(Category.class.getName(), categoryDAO);
    }

    private void initStockRelatedDAOSMap() {
        daosMap.put(Stock.class.getName(), stockDAO);
        daosMap.put(StockHistory.class.getName(), stockHistoryDAO);
    }

    private void initSaleRelatedDAOSMap() {
        daosMap.put(Sale.class.getName(), saleDAO);
        daosMap.put(SaleItem.class.getName(), saleItemDAO);
        daosMap.put(SaleAddress.class.getName(), saleAddressDAO);
        daosMap.put(SaleCreditCard.class.getName(), saleCreditCardDAO);
    }

    private void initVoucherRelatedDAOSMap() {
        daosMap.put(Voucher.class.getName(), voucherDAO);
        daosMap.put(UserVoucher.class.getName(), customerVoucherDAO);
    }

    private void initTradeRelatedDAOSMap() {
        daosMap.put(Trade.class.getName(), tradeDAO);
        daosMap.put(TradeItem.class.getName(), tradeItemDAO);
    }

    @Override
    public T save(T entity) {
        T savedEntity = null;
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DAO<T> dao = (DAO<T>) daosMap.get(entityName);
            savedEntity = dao.save(entity);
        }

        return savedEntity;
    }

    @Override
    public T saveAndFlush(T entity) {
        T savedEntity = null;
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DAO<T> dao = (DAO<T>) daosMap.get(entityName);
            savedEntity = dao.saveAndFlush(entity);
        }

        return savedEntity;
    }

    @Override
    public void remove(T entity) {
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            daosMap.get(entityName).delete(entity);
        }
    }

    @Override
    public Optional<T> findById(Long id, T entity) {
        Optional<T> optionalEntity = Optional.empty();
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            optionalEntity = daosMap.get(entityName).findById(id);
        }

        return optionalEntity;
    }

    @Override
    public Optional<T> findBy(Entity targetEntity, T baseEntity) {
        Optional<T> optionalEntity = Optional.empty();
        String entityName = baseEntity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DAO<T> selectedDAO = daosMap.get(entityName);
            optionalEntity = executeFindBy(targetEntity, selectedDAO);
        }

        return optionalEntity;
    }

    @Override
    public List<T> findAll(T entity) {
        List<T> entitiesCollection = Collections.EMPTY_LIST;
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DAO<T> selectedDAO = daosMap.get(entityName);

            if (selectedDAO instanceof DateFilter) {
                entitiesCollection = (List<T>) ((DateFilter) selectedDAO).findAllByOrderByDateDesc();
            } else {
                entitiesCollection = selectedDAO.findAll();
            }
        }

        return entitiesCollection;
    }

    @Override
    public List<T> findAllBy(Entity targetEntity, T baseEntity) {
        List<T> entitiesCollection = Collections.EMPTY_LIST;
        String entityName = baseEntity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DAO<T> selectedDAO = daosMap.get(entityName);
            entitiesCollection = executeFindAllBy(targetEntity, selectedDAO);
        }

        return entitiesCollection;
    }

    @Override
    public List<T> findAllValidBy(Entity targetEntity, T baseEntity) {
        List<T> entitiesCollection = Collections.EMPTY_LIST;
        String entityName = baseEntity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DAO<T> selectedDAO = daosMap.get(entityName);
            entitiesCollection = executeFindAllValidBy(targetEntity, selectedDAO);
        }

        return entitiesCollection;
    }

    @Override
    public Optional<T> findValidByEmbeddedEntity(AssociativeDomainEntity associativeEntity, Entity entityOne, Entity entityTwo) {
        Optional<T> optional = Optional.empty();
        String entityName = associativeEntity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DAO<T> selectedDAO = daosMap.get(entityName);
            optional = executeFindValidByEntityOneAndEntityTwo(entityOne, entityTwo, selectedDAO);
        }

        return optional;
    }

    @Override
    public Optional<T> findActivatedById(Long id, Entity entity) {
        Optional<T> optionalEntity = Optional.empty();
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DomainDAO<T> selectedDAO = (DomainDAO<T>) daosMap.get(entityName);
            optionalEntity = selectedDAO.findByIdAndInactivatedFalse(id);
        }

        return optionalEntity;
    }

    @Override
    public Optional<T> findInactivatedById(Long id, Entity entity) {
        Optional<T> optionalEntity = Optional.empty();
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DomainDAO<T> selectedDAO = (DomainDAO<T>) daosMap.get(entityName);
            optionalEntity = selectedDAO.findByIdAndInactivatedTrue(id);
        }

        return optionalEntity;
    }

    @Override
    public List<T> findAllActivatedBy(T entity) {
        List<T> entitiesCollection = Collections.EMPTY_LIST;
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DomainDAO<T> selectedDAO = (DomainDAO<T>) daosMap.get(entityName);
            entitiesCollection = selectedDAO.findAllByInactivatedFalse();
        }

        return entitiesCollection;
    }

    @Override
    public List<T> findAllInactivatedBy(T entity) {
        List<T> entitiesCollection = Collections.EMPTY_LIST;
        String entityName = entity.getClass().getName();

        if (daosMap.containsKey(entityName)) {
            DomainDAO<T> selectedDAO = (DomainDAO<T>) daosMap.get(entityName);
            entitiesCollection = selectedDAO.findAllByInactivatedTrue();
        }

        return entitiesCollection;
    }

    private Optional<T> executeFindBy(Entity filterEntity, DAO<T> dao) {
        Optional<T> optionalEntity = Optional.empty();

        if (filterEntity instanceof User) {
            User user = (User) filterEntity;

            if (dao instanceof CustomerDAO) {
                CustomerDAO customerDAO = (CustomerDAO) dao;
                optionalEntity = (Optional<T>) customerDAO.findCustomerByUser(user);
            }
        }

        return optionalEntity;
    }

    private List<T> executeFindAllBy(Entity filterEntity, DAO<T> dao) {
        List<T> entities = Collections.EMPTY_LIST;

        if (filterEntity instanceof Customer) {
            Customer customer = (Customer) filterEntity;

            if (dao instanceof CreditCardDAO) {
                CreditCardDAO creditCardDAO = (CreditCardDAO) dao;
                entities = (List<T>) creditCardDAO.findAllByCustomer(customer);
            }
            if (dao instanceof UserVoucherDAO) {
                UserVoucherDAO customerVoucherDAO = (UserVoucherDAO) dao;
                entities = (List<T>) customerVoucherDAO.findCustomerVoucherByCustomer(customer);
            }
            if (dao instanceof SaleDAO) {
                SaleDAO saleDAO = (SaleDAO) dao;
                entities = (List<T>) saleDAO.findAllByCustomer(customer);
            }
            if (dao instanceof TradeDAO) {
                TradeDAO tradeDAO = (TradeDAO) dao;
                entities = (List<T>) tradeDAO.findAllByOrderCustomer(customer);
            }
        }

        return entities;
    }

    private List<T> executeFindAllValidBy(Entity filterEntity, DAO<T> dao) {
        List<T> entities = Collections.EMPTY_LIST;

        if (filterEntity instanceof Customer) {
            Customer customer = (Customer) filterEntity;

            if (dao instanceof UserVoucherDAO) {
                UserVoucherDAO customerVoucherDAO = (UserVoucherDAO) dao;
                entities = (List<T>) customerVoucherDAO.findCustomerVoucherByUsedFalseAndCustomer(customer);
            }
        }

        return entities;
    }

    private Optional<T> executeFindValidByEntityOneAndEntityTwo(Entity filterEntityOne,
                                                                Entity filterEntityTwo,
                                                                DAO<T> dao) {
        Optional<T> optional = Optional.empty();

        if (filterEntityOne instanceof Customer && filterEntityTwo instanceof Voucher) {
            Customer customer = (Customer) filterEntityOne;
            Voucher voucher = (Voucher) filterEntityTwo;

            if (dao instanceof UserVoucherDAO) {
                UserVoucherDAO userVoucherDAO = (UserVoucherDAO) dao;
                optional = (Optional<T>) userVoucherDAO
                        .findByUsedFalseAndCustomerAndVoucher(customer, voucher);
            }
        }

        return optional;
    }
}
