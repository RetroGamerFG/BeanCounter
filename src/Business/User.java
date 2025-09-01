//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// User - holds information about a user if the software is set to multiple users mode.

package Business;

import java.io.Serializable;

public class User implements Serializable
{
    private String firstName;
    private String lastName;
    private String username;
    private String password;

    private boolean adminStatus;
    private boolean activeStatus;

    public User(String username, String password, String firstName, String lastName)
    {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;

        adminStatus = false;
        activeStatus = true;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean getActiveStatus()
    {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus)
    {
        this.activeStatus = activeStatus;
    }

//
// Additional Functions
//

    public boolean isAdmin()
    {
        return this.adminStatus;
    }

    public void setAdminStatus(boolean setVal)
    {
        this.adminStatus = setVal;
    }
}
