locals {
  tags = (merge(
    var.common_tags,
    tomap({
      "Team Contact" = var.team_contact
      "Destroy Me"   = var.destroy_me
    })
  ))
  civil_service_resource_group = "${var.product}-service-${var.env}"
  civil_shared_resource_group = "${var.product}-shared-${var.env}"
}
