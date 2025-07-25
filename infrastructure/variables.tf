variable "product" {
  type        = string
  description = "Product name visible on azure infrastructure (application name)"
  default     = "civil"
}

variable "component" {}

variable "location" {
  type    = string
  default = "UK South"
}

variable "env" {}

variable "subscription" {}

variable "common_tags" {
  type = map(string)
}

variable "team_contact" {
  type        = string
  description = "The name of your Slack channel people can use to contact your team about your infrastructure"
  default     = "#civil-sdt"
}

variable "destroy_me" {
  type        = string
  description = "Here be dragons! In the future if this is set to Yes then automation will delete this resource on a schedule. Please set to No unless you know what you are doing"
  default     = "No"
}

variable "sku" {
  type        = string
  default     = "Premium"
  description = "SKU type(Basic, Standard and Premium)"
}

variable "tenant_id" {
  type        = string
  description = "(Required) The Azure Active Directory tenant ID that should be used for authenticating requests to the key vault. This is usually sourced from environment variables and not normally required to be specified."
}

variable "jenkins_AAD_objectId" {
  type        = string
  description = "(Required) The Azure AD object ID of a user, service principal or security group in the Azure Active Directory tenant for the vault. The object ID must be unique for the list of access policies."
}

variable "aks_subscription_id" {}

variable "database_backup_retention_days" {
  default     = 35
  description = "Backup retention period in days for the PGSql instance. Valid values are between 7 & 35 days"
}

variable "max_message_size_in_kilobytes" {
  type        = string
  description = "Integer value which controls the maximum size of a message allowed on the queue for Premium SKU"
  default     = null
}

variable "kv_subscription" {
  type        = string
  description = "Name or Id of key vault subscription.  Used for setting up privileges for database reporting postgres cron jobs.  Only applies to production."
  default     = "DCD-CNP-DEV"
}
