# Create the database server
# Name and resource group name will be defaults (<product>-<component>-<env> and <product>-<component>-data-<env> respectively)
module "postgresql-v15" {
  source = "git@github.com:hmcts/terraform-module-postgresql-flexible?ref=master"

  providers = {
    azurerm.postgres_network = azurerm.private_endpoint
  }

  admin_user_object_id        = var.jenkins_AAD_objectId
  business_area               = "cft"
  common_tags                 = local.tags
  component                   = var.component
  env                         = var.env
  enable_db_report_privileges = true
  pgsql_databases = [
    {
      name : "civil_sdt"
      report_privilege_schema : "public"
      report_privilege_tables : ["individual_requests"]
    }
  ]
  kv_name                            = module.civil_sdt_key_vault.key_vault_name
  kv_subscription                    = var.kv_subscription
  user_secret_name                   = azurerm_key_vault_secret.POSTGRES-USER-V15.name
  pass_secret_name                   = azurerm_key_vault_secret.POSTGRES-PASS-V15.name
  force_db_report_privileges_trigger = "1"
  pgsql_version                      = "15"
  product                            = var.product
  name                               = join("-", [var.product, var.component, "v15"])
  backup_retention_days              = var.database_backup_retention_days
}

# Create secret for database user
resource "azurerm_key_vault_secret" "POSTGRES-USER-V15" {
  name         = "civil-sdt-POSTGRES-USER-V15"
  value        = module.postgresql-v15.username
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}

# Create secret for database password
resource "azurerm_key_vault_secret" "POSTGRES-PASS-V15" {
  name         = "civil-sdt-POSTGRES-PASS-V15"
  value        = module.postgresql-v15.password
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}

# Create secret for database host
resource "azurerm_key_vault_secret" "POSTGRES-HOST-V15" {
  name         = "civil-sdt-POSTGRES-HOST-V15"
  value        = module.postgresql-v15.fqdn
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}
