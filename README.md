<h1>BeanCounter</h1>
An accounting program written in Java. Built on Spring Boot, with an approach for an easy to use interface for all users. Primarily aimed for individuals and small business owners. All data is local, with no cloud or external database dependencies. No monthly fees or annual costs.

Log transactions, generate statements, and perform additional functions for your bookkeeping needs.

<h2>Features</h2>

• [X] Create, edit, review, & post journal entries

• [-] Lookup posted entries

• [-] Add custom new account codes

• [-] Generate statements such as income, balance sheet, etc.

• [ ] Manual backup of all transactions and generated 

• [X] Business setup for name and fiscal year setup used by statements

• [-] (Optional) RESTfulAPI integration that utilizes AI agents and MCP servers.

• [ ] Additional functionaly specified by users

<h2>Testers Wanted!</h2>
I'm looking for users that can test out the program. This includes general usage, bug finding, possible calculation errors, and anything else that throws a wrench into the machine. Any feedback during development is valuable, and is greatly appreciated.

<h2>Lastest Version</h2>
An early version of the first-time setup has been created, and the income statement is completed. It includes the ability to generate income statements by month, quarter, and year, with the extra option to include the months in a quarter, and the quarters in a year.

<h2>Next Update</h2>
Next will be the balance sheet. It follows the same logic as the income statement, so I don't expect it to take too long. Eventually I want to revisit both to better order accounts, such as specifying 'cost of goods sold' as a contra to sales revenue instead of as an expense line. It will require rewriting the Account class, so my focus for now will be proceeding with major features.

<h2>MCP & Agent Functionality</h2>
I am looking into optional packages that can utilize AI agents and MCP servers. However, I am still debating whether to include the prepared tools in this version. This functionality goes beyond the initial scope of the project, so I am still considering whether this will be part of a premium version of BeanCounter for the future. To be clear, the RESTful API will be included and is completely optional, but the tools/resources/prompts will not.

<h2>Notice</h2>
While this software is still being developed, it is likely for bugs or unintended errors to occur. Please use this software at your own risk and liability in the event of such a problem occurring.
