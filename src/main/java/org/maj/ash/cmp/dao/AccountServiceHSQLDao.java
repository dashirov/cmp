package org.maj.ash.cmp.dao;

import org.maj.ash.cmp.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.SortedSet;

/**
 * Created by shamikm78 on 11/6/16.
 */
public class AccountServiceHSQLDao implements AccountServiceDao {
    private JdbcTemplate jdbcTemplate;

    public void setDatasource(DataSource datasource){
        jdbcTemplate  = new JdbcTemplate(datasource);
    }

    @Override
    public Account saveAccount(Account account) {
        return null;
    }

    @Override
    public Account retrieveAccount(Long accountId) {
        return null;
    }

    @Override
    public MSAAccount saveMSAAccount(final MSAAccount msaAccount) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement("INSERT INTO account(name,description,type) values(?,?,?)",new String[]{"id"});
                int index = 1;
                ps.setString(index++,msaAccount.getName());
                ps.setString(index++,msaAccount.getDescription());
                ps.setString(index++,msaAccount.getType().name());
                return ps;
            }
        },keyHolder);

        return retrieveMSAAccount(keyHolder.getKey().longValue());
    }

    @Override
    public MSAAccount retrieveMSAAccount(Long msaAccountId) {
        return jdbcTemplate.queryForObject("SELECT name,description from account where id = ?", new RowMapper<MSAAccount>() {
            @Override
            public MSAAccount mapRow(ResultSet resultSet, int i) throws SQLException {
                MSAAccount account = new MSAAccount();
                int index = 1;
                account.setName(resultSet.getString(index++));
                account.setDescription(resultSet.getString(index++));
                return account;
            }
        },msaAccountId);
    }

    @Override
    public BusinessUnit saveBusinessUnit(BusinessUnit businessUnit) {
        return null;
    }

    @Override
    public BusinessUnit retrieveBusinessUnit(Long businessUnitId) {
        return null;
    }

    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public Product retrieveProduct(String productCode) {
        return null;
    }

    @Override
    public Campaign saveCampaign(Campaign campaign) {
        return null;
    }

    @Override
    public Campaign retrieveCampaign(String campaignId) {
        return null;
    }

    @Override
    public Marketplace saveMarketplace(Marketplace marketplace) {
        return null;
    }

    @Override
    public Marketplace retrieveMarketplace(Long marketplaceId) {
        return null;
    }

    @Override
    public SortedSet<Marketplace> listMarketplaces() {
        return null;
    }

    @Override
    public Marketplace markMarketplaceForTermination(Long marketplaceId) {
        return null;
    }

    @Override
    public Marketplace markMarketplaceForTermination(Marketplace marketplace) {
        return null;
    }

    @Override
    public Marketplace markMarketplaceForTermination(Date effectiveDate, Marketplace marketplace) {
        return null;
    }

    @Override
    public SortedSet<Product> listAccountProducts(Account account) {
        return null;
    }

    @Override
    public SortedSet<Product> listAccountProducts(Long accountId) {
        return null;
    }

    @Override
    public SortedSet<Product> listAccountProductsInHierarchy(Account account) {
        return null;
    }

    @Override
    public SortedSet<Product> listAccountProductsInHierarchy(Long accountId) {
        return null;
    }

    @Override
    public SortedSet<Account> listAccounts(Account account) {
        return null;
    }

    @Override
    public SortedSet<Account> listAccounts(Long accountId) {
        return null;
    }

    @Override
    public SortedSet<Account> listAccountsInHierarchy(Account account) {
        return null;
    }

    @Override
    public SortedSet<Account> listAccountsInHierarchy(Long accountId) {
        return null;
    }

    @Override
    public SortedSet<Campaign> listAccountCampaigns(Account account) {
        return null;
    }

    @Override
    public SortedSet<Campaign> listAccountCampaigns(Long accountId) {
        return null;
    }

    @Override
    public SortedSet<Campaign> listAccountCampaignsInHierarchy(Account account) {
        return null;
    }

    @Override
    public SortedSet<Campaign> listAccountCampaignsInHierarchy(Long accountId) {
        return null;
    }

    @Override
    public SortedSet<Campaign> listProductCampaigns(Product product) {
        return null;
    }

    @Override
    public SortedSet<Campaign> listProductCampaigns(String productCode) {
        return null;
    }
}
