//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Business - the base class for business. Contains user/business information that is carried over to specific forms.

package Business;

public class Business
{
    private String businessName;
    
    //need to figure out more about what this class would entail.

    public Business()
    {
        businessName = "Placeholder";
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }
}
