package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.entities.Token;
import edu.ut.sales.sales_analyst.model.entities.User;

public interface ITokenService {

    Token addToken(User user, String token);

    Token refreshToken(String refreshToken, User user) throws Exception;

    Boolean revokeToken(String token);
}
