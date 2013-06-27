package uk.gov.moj.sdt.validators.exception.api;

public interface IBusinessException
{

    /**
     * Get the error code.
     * 
     * @return error code
     */
    String getErrorCode ();

    /**
     * Get the error description.
     * 
     * @return error description
     */
    String getErrorDescription ();
}
