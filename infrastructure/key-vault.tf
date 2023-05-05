data "azurerm_key_vault" "civil_vault" {
  name                = "civil-${var.env}"
  resource_group_name = local.civil_resource_group
}

data "azurerm_key_vault" "s2s_vault" {
  name                = "s2s-${var.env}"
  resource_group_name = "rpe-service-auth-provider-${var.env}"
}

#data "azurerm_key_vault_secret" "civil_sdt_service_s2s_key" {
#  name         = "microservicekey-civil-sdt-service"
#  key_vault_id = data.azurerm_key_vault.s2s_vault.id
#}
#
#resource "azurerm_key_vault_secret" "civil_sdt_service_s2s_secret" {
#  name         = "civil-sdt-service-s2s-secret"
#  value        = data.azurerm_key_vault_secret.civil_sdt_service_s2s_key.value
#  key_vault_id = data.azurerm_key_vault.civil_vault.id
#}

data "azurerm_key_vault_secret" "api_gw_s2s_key" {
  name         = "microservicekey-api-gw"
  key_vault_id = data.azurerm_key_vault.s2s_vault.id
}

resource "azurerm_key_vault_secret" "api_gw_s2s_secret" {
  name         = "api-gateway-s2s-secret"
  value        = data.azurerm_key_vault_secret.api_gw_s2s_key.value
  key_vault_id = data.azurerm_key_vault.civil_vault.id
}
