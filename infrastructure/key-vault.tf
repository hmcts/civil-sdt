data "azurerm_user_assigned_identity" "civil-mi" {
  name                = "${var.product}-${var.env}-mi"
  resource_group_name = "managed-identities-${var.env}-rg"
}

module "civil_sdt_key_vault" {
  source = "git@github.com:hmcts/cnp-module-key-vault?ref=master"

  name                        = "${var.product}-${var.component}-${var.env}"
  product                     = var.product
  env                         = var.env
  object_id                   = var.jenkins_AAD_objectId
  resource_group_name         = azurerm_resource_group.civil_sdt_rg.name
  product_group_name          = "DTS Civil"
  common_tags                 = local.tags
  managed_identity_object_ids = [data.azurerm_user_assigned_identity.civil-mi.principal_id]
}

data "azurerm_key_vault" "s2s_vault" {
  name                = "s2s-${var.env}"
  resource_group_name = "rpe-service-auth-provider-${var.env}"
}

data "azurerm_key_vault_secret" "key_from_vault" {
  name         = "microservicekey-civil-sdt"
  key_vault_id = data.azurerm_key_vault.s2s_vault.id
}

resource "azurerm_key_vault_secret" "s2s" {
  name         = "civil-sdt-service-s2s-secret"
  value        = data.azurerm_key_vault_secret.key_from_vault.value
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}

# Set up prod key vault access for DTS Production DB Reporting principle.
# This is required for AM Dashboard Daily Updates Azure pipeline.
data "azurerm_client_config" "current" {}

data "azuread_service_principal" "dts_prod_db_reporting" {
  display_name = "DTS Production DB Reporting"
}

resource "azurerm_key_vault_access_policy" "dts_prod_db_reporting_policy" {
  count = var.env == "prod" ? 1 : 0

  key_vault_id = module.civil_sdt_key_vault.key_vault_id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = data.azuread_service_principal.dts_prod_db_reporting.object_id

  secret_permissions = [
    "Get",
    "List"
  ]
}
