locals {
  tags = (merge(
    var.common_tags,
    tomap({
      "Team Contact" = var.team_contact
      "Destroy Me"   = var.destroy_me
    })
  ))
  civil_shared_resource_group = "${var.product}-shared-${var.env}"
}

resource "azurerm_resource_group" "civil_sdt_rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = var.location
}
