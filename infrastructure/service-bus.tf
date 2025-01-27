module "servicebus-namespace" {
  providers = {
    azurerm.private_endpoint = azurerm.private_endpoint
  }
  source              = "git@github.com:hmcts/terraform-module-servicebus-namespace?ref=4.x"
  name                = "${var.product}-${var.component}-servicebus-${var.env}"
  resource_group_name = azurerm_resource_group.civil_sdt_rg.name
  location            = var.location
  env                 = var.env
  common_tags         = local.tags
  sku                 = var.sku
}

module "servicebus-sdt-queue" {
  source                        = "git@github.com:hmcts/terraform-module-servicebus-queue?ref=4.x"
  name                          = "${var.product}-${var.component}-in-out-${var.env}"
  namespace_name                = module.servicebus-namespace.name
  resource_group_name           = azurerm_resource_group.civil_sdt_rg.name
  max_message_size_in_kilobytes = var.max_message_size_in_kilobytes

  depends_on = [module.servicebus-namespace]
}

resource "azurerm_key_vault_secret" "servicebus_primary_connection_string" {
  name         = "civil-sdt-servicebus-connection-string"
  value        = module.servicebus-namespace.primary_send_and_listen_connection_string
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}

resource "azurerm_key_vault_secret" "servicebus_primary_shared_access_key" {
  name         = "civil-sdt-servicebus-shared-access-key"
  value        = module.servicebus-namespace.primary_send_and_listen_shared_access_key
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}

# Create secret used for setting spring.jms.servicebus.pricing-tier property.
# Set value to "Standard" for all environments, even those that have a Premium tier service bus.
# This is a work around for an issue where SDT does not read messages from a Premium tier service
# bus in a timely fashion.
resource "azurerm_key_vault_secret" "servicebus-pricing-tier" {
  name         = "civil-sdt-servicebus-pricing-tier"
  value        = "Standard"
  key_vault_id = module.civil_sdt_key_vault.key_vault_id
}
