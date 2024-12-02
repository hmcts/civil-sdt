module "application_insights" {
  source = "git@github.com:hmcts/terraform-module-application-insights?ref=4.x"

  env                 = var.env
  product             = var.product
  name                = "${var.product}-${var.component}-appinsights"
  location            = var.location
  resource_group_name = azurerm_resource_group.civil_sdt_rg.name

  common_tags = var.common_tags
}

moved {
  from = azurerm_application_insights.appinsights
  to   = module.application_insights.azurerm_application_insights.this
}

resource "azurerm_key_vault_secret" "appinsights-connection-string" {
  name         = "civil-sdt-appinsights-connection-string"
  value        = module.application_insights.connection_string
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}
