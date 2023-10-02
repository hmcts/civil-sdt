# Create the database server
# Name and resource group name will be defaults (<product>-<component>-<env> and <product>-<component>-data-<env> respectively)
module "postgresql" {
  source = "git@github.com:hmcts/terraform-module-postgresql-flexible?ref=master"

  providers = {
    azurerm.postgres_network = azurerm.private_endpoint
  }

  admin_user_object_id  = var.jenkins_AAD_objectId
  business_area         = "cft"
  common_tags           = local.tags
  component             = var.component
  env                   = var.env
  pgsql_databases = [
    {
      name = "civil_sdt"
    }
  ]
  pgsql_version         = "11"
  product               = var.product
  backup_retention_days = "${var.database_backup_retention_days}"
}

# Create secret for database user
resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name         = "civil-sdt-POSTGRES-USER"
  value        = module.postgresql.username
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}

# Create secret for database password
resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name         = "civil-sdt-POSTGRES-PASS"
  value        = module.postgresql.password
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}

# Create secret for database host
resource "azurerm_key_vault_secret" "POSTGRES-HOST" {
  name         = "civil-sdt-POSTGRES-HOST"
  value        = module.postgresql.fqdn
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}


module "postgresql-v15" {
  source = "git@github.com:hmcts/terraform-module-postgresql-flexible?ref=master"

  providers = {
    azurerm.postgres_network = azurerm.private_endpoint
  }

  admin_user_object_id = var.jenkins_AAD_objectId
  business_area        = "cft"
  common_tags          = local.tags
  component            = var.component-V15
  env                  = var.env
  pgsql_databases = [
    {
      name = "civil_sdt"
    }
  ]
  pgsql_version        = "15"
  product              = var.product
  name               = join("-", [var.product, var.component-V15])
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
