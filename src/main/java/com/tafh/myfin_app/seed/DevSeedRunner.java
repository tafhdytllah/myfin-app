package com.tafh.myfin_app.seed;

import com.tafh.myfin_app.account.model.AccountEntity;
import com.tafh.myfin_app.account.repository.AccountRepository;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.category.repository.CategoryRepository;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import com.tafh.myfin_app.user.model.RoleEnum;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DevSeedRunner implements CommandLineRunner {

    private final DevSeedProperties properties;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) {
        if (!properties.isEnabled()) {
            return;
        }

        if (userRepository.findByUsername(properties.getDemoUsername()).isPresent()) {
            return;
        }

        UserEntity user = userRepository.save(UserEntity.create(
                properties.getDemoUsername(),
                properties.getDemoEmail(),
                passwordEncoder.encode(properties.getDemoPassword()),
                RoleEnum.USER
        ));

        Map<String, AccountEntity> accounts = seedAccounts(user);
        Map<String, CategoryEntity> categories = seedCategories(user);

        seedTransactions(accounts, categories);
    }

    private Map<String, AccountEntity> seedAccounts(UserEntity user) {
        Map<String, AccountEntity> accounts = new LinkedHashMap<>();

        accounts.put("Main Wallet", accountRepository.save(AccountEntity.create(
                user,
                "Main Wallet",
                BigDecimal.ZERO
        )));
        accounts.put("BCA Savings", accountRepository.save(AccountEntity.create(
                user,
                "BCA Savings",
                BigDecimal.ZERO
        )));
        accounts.put("Emergency Fund", accountRepository.save(AccountEntity.create(
                user,
                "Emergency Fund",
                BigDecimal.ZERO
        )));

        return accounts;
    }

    private Map<String, CategoryEntity> seedCategories(UserEntity user) {
        Map<String, CategoryEntity> categories = new LinkedHashMap<>();

        categories.put("Salary", createCategory(user, "Salary", CategoryType.INCOME));
        categories.put("Freelance", createCategory(user, "Freelance", CategoryType.INCOME));
        categories.put("Bonus", createCategory(user, "Bonus", CategoryType.INCOME));
        categories.put("Food", createCategory(user, "Food", CategoryType.EXPENSE));
        categories.put("Transport", createCategory(user, "Transport", CategoryType.EXPENSE));
        categories.put("Rent", createCategory(user, "Rent", CategoryType.EXPENSE));
        categories.put("Shopping", createCategory(user, "Shopping", CategoryType.EXPENSE));
        categories.put("Bills", createCategory(user, "Bills", CategoryType.EXPENSE));
        categories.put("Health", createCategory(user, "Health", CategoryType.EXPENSE));
        categories.put("Entertainment", createCategory(user, "Entertainment", CategoryType.EXPENSE));

        return categories;
    }

    private CategoryEntity createCategory(UserEntity user, String name, CategoryType type) {
        return categoryRepository.save(CategoryEntity.create(user, name, type));
    }

    private void seedTransactions(
            Map<String, AccountEntity> accounts,
            Map<String, CategoryEntity> categories
    ) {
        LocalDateTime now = LocalDateTime.now();

        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Salary"),
                new BigDecimal("8500000"),
                CategoryType.INCOME,
                "Monthly salary",
                now.minusMonths(3).withDayOfMonth(1).withHour(9).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Freelance"),
                new BigDecimal("1250000"),
                CategoryType.INCOME,
                "Landing page freelance project",
                now.minusMonths(3).withDayOfMonth(8).withHour(20).withMinute(30)
        );
        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Rent"),
                new BigDecimal("2500000"),
                CategoryType.EXPENSE,
                "Monthly rent payment",
                now.minusMonths(3).withDayOfMonth(3).withHour(8).withMinute(15)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Food"),
                new BigDecimal("185000"),
                CategoryType.EXPENSE,
                "Weekend groceries",
                now.minusMonths(3).withDayOfMonth(5).withHour(17).withMinute(45)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Transport"),
                new BigDecimal("96000"),
                CategoryType.EXPENSE,
                "Fuel and parking",
                now.minusMonths(3).withDayOfMonth(7).withHour(18).withMinute(5)
        );

        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Salary"),
                new BigDecimal("8500000"),
                CategoryType.INCOME,
                "Monthly salary",
                now.minusMonths(2).withDayOfMonth(1).withHour(9).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Bonus"),
                new BigDecimal("1000000"),
                CategoryType.INCOME,
                "Quarterly performance bonus",
                now.minusMonths(2).withDayOfMonth(12).withHour(11).withMinute(0)
        );
        createTransaction(
                accounts.get("Emergency Fund"),
                categories.get("Freelance"),
                new BigDecimal("2250000"),
                CategoryType.INCOME,
                "Backend API freelance milestone",
                now.minusMonths(2).withDayOfMonth(16).withHour(14).withMinute(20)
        );
        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Bills"),
                new BigDecimal("675000"),
                CategoryType.EXPENSE,
                "Electricity and internet bills",
                now.minusMonths(2).withDayOfMonth(4).withHour(21).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Shopping"),
                new BigDecimal("540000"),
                CategoryType.EXPENSE,
                "Office desk accessories",
                now.minusMonths(2).withDayOfMonth(18).withHour(19).withMinute(15)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Entertainment"),
                new BigDecimal("220000"),
                CategoryType.EXPENSE,
                "Cinema and dinner",
                now.minusMonths(2).withDayOfMonth(22).withHour(20).withMinute(10)
        );

        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Salary"),
                new BigDecimal("8750000"),
                CategoryType.INCOME,
                "Monthly salary",
                now.minusMonths(1).withDayOfMonth(1).withHour(9).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Freelance"),
                new BigDecimal("1750000"),
                CategoryType.INCOME,
                "UI slicing project payment",
                now.minusMonths(1).withDayOfMonth(10).withHour(13).withMinute(0)
        );
        createTransaction(
                accounts.get("Emergency Fund"),
                categories.get("Bonus"),
                new BigDecimal("1500000"),
                CategoryType.INCOME,
                "Year-end bonus reserve",
                now.minusMonths(1).withDayOfMonth(15).withHour(10).withMinute(0)
        );
        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Rent"),
                new BigDecimal("2500000"),
                CategoryType.EXPENSE,
                "Monthly rent payment",
                now.minusMonths(1).withDayOfMonth(3).withHour(8).withMinute(15)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Food"),
                new BigDecimal("310000"),
                CategoryType.EXPENSE,
                "Family dinner and groceries",
                now.minusMonths(1).withDayOfMonth(6).withHour(19).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Health"),
                new BigDecimal("420000"),
                CategoryType.EXPENSE,
                "Clinic checkup and medicine",
                now.minusMonths(1).withDayOfMonth(17).withHour(9).withMinute(30)
        );

        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Salary"),
                new BigDecimal("8750000"),
                CategoryType.INCOME,
                "Monthly salary",
                now.withDayOfMonth(1).withHour(9).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Freelance"),
                new BigDecimal("950000"),
                CategoryType.INCOME,
                "Small website maintenance retainer",
                now.withDayOfMonth(9).withHour(15).withMinute(10)
        );
        createTransaction(
                accounts.get("Emergency Fund"),
                categories.get("Bonus"),
                new BigDecimal("500000"),
                CategoryType.INCOME,
                "Cashback and bank reward",
                now.withDayOfMonth(11).withHour(12).withMinute(0)
        );
        createTransaction(
                accounts.get("BCA Savings"),
                categories.get("Rent"),
                new BigDecimal("2500000"),
                CategoryType.EXPENSE,
                "Monthly rent payment",
                now.withDayOfMonth(3).withHour(8).withMinute(15)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Food"),
                new BigDecimal("265000"),
                CategoryType.EXPENSE,
                "Coffee, lunch, and groceries",
                now.withDayOfMonth(5).withHour(18).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Transport"),
                new BigDecimal("145000"),
                CategoryType.EXPENSE,
                "Ride hailing and fuel",
                now.withDayOfMonth(7).withHour(9).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Bills"),
                new BigDecimal("780000"),
                CategoryType.EXPENSE,
                "Phone, internet, and utilities",
                now.withDayOfMonth(8).withHour(20).withMinute(0)
        );
        createTransaction(
                accounts.get("Main Wallet"),
                categories.get("Shopping"),
                new BigDecimal("690000"),
                CategoryType.EXPENSE,
                "Mechanical keyboard purchase",
                now.withDayOfMonth(14).withHour(21).withMinute(15)
        );
    }

    private void createTransaction(
            AccountEntity account,
            CategoryEntity category,
            BigDecimal amount,
            CategoryType type,
            String description,
            LocalDateTime createdAt
    ) {
        if (type == CategoryType.INCOME) {
            account.increaseBalance(amount);
        } else {
            account.decreaseBalance(amount);
        }

        TransactionEntity transaction = transactionRepository.save(
                TransactionEntity.create(account, category, amount, type, description)
        );

        jdbcTemplate.update(
                "UPDATE transactions SET created_at = ?, updated_at = ? WHERE id = ?",
                Timestamp.valueOf(createdAt),
                Timestamp.valueOf(createdAt),
                transaction.getId()
        );
    }
}
