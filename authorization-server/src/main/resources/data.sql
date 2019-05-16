insert into oauth_client_details (client_id, client_secret,
    resource_ids, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information,
    autoapprove, logout_uri, base_uri)
  values ('System1_id', 'System1_secret',
    null, 'read', 'authorization_code',
    'http://localhost:18010/oauthCallback', 'ROLE_YOUR_CLIENT', 36000,
    2592000, null,
    'true', 'http://localhost:8081/logout', 'http://localhost:8081/me');

insert into oauth_client_details (client_id, client_secret,
    resource_ids, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information,
    autoapprove, logout_uri, base_uri)
  values ('System2_id', 'System2_secret',
    null, 'read', 'authorization_code',
    'http://localhost:18020/oauthCallback', 'ROLE_YOUR_CLIENT', 36000,
    2592000, null,
    'true', 'http://localhost:18020/logout', 'http://localhost:18020/me');

insert into oauth_client_details (client_id, client_secret,
    resource_ids, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information,
    autoapprove, logout_uri, base_uri)
  values ('System3_id', 'System3_secret',
    null, 'read', 'authorization_code',
    'http://localhost:18030/oauthCallback', 'ROLE_YOUR_CLIENT', 36000,
    2592000, null,
    'true', 'http://localhost:18030/logout', 'http://localhost:18030/me');

insert into oauth_client_details (client_id, client_secret,
    resource_ids, scope, authorized_grant_types,
    web_server_redirect_uri, authorities, access_token_validity,
    refresh_token_validity, additional_information,
    autoapprove, logout_uri, base_uri)
  values ('System4_id', 'System4_secret',
    null, 'read', 'authorization_code',
    'http://localhost:18040/oauthCallback', 'ROLE_YOUR_CLIENT', 36000,
    2592000, null,
    'true', 'http://localhost:18040/logout', 'http://localhost:18040/me');