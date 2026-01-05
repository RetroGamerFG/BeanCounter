-- Assets (A)
INSERT INTO Account (AccountCode, AccountName, AccountDescription, AccountType, IsContra) VALUES
    ('1000.001', 'Cash', 'Cash on hand and from operations', 'A', FALSE),       -- Cash
    ('1000.002', 'Bank', 'Funds held in checking or savings accounts', 'A', FALSE),  -- Bank Account
    ('1100.001', 'Accounts Receivable', 'Money owed by customers', 'A', FALSE),     -- Accounts Receivable
    ('1200.001', 'Inventory', 'Goods held for resale', 'A', FALSE),               -- Inventory
    ('1300.001', 'Prepaid Expenses', 'Payments made for future expenses', 'A', FALSE); -- Prepaid Expenses

-- Liabilities (L)
INSERT INTO Account (AccountCode, AccountName, AccountDescription, AccountType, IsContra) VALUES
    ('2000.001', 'Accounts Payable', 'Money owed to vendors or suppliers', 'L', FALSE), -- Accounts Payable
    ('2100.001', 'Notes Payable', 'Debt obligations or loans owed', 'L', FALSE),         -- Notes Payable
    ('2200.001', 'Accrued Liabilities', 'Expenses that have been incurred but not paid', 'L', FALSE); -- Accrued Liabilities

-- Equity (E)
INSERT INTO Account (AccountCode, AccountName, AccountDescription, AccountType, IsContra) VALUES
    ('3000.001', 'Owner’s Equity', 'Owner’s investment in the business', 'E', FALSE), -- Owner’s Equity
    ('3100.001', 'Retained Earnings', 'Profits that are retained in the company', 'E', FALSE), -- Retained Earnings
    ('3200.001', 'Common Stock', 'Shares issued to owners of the company', 'E', FALSE); -- Common Stock

-- Revenues (R)
INSERT INTO Account (AccountCode, AccountName, AccountDescription, AccountType, IsContra) VALUES
    ('4000.001', 'Sales Revenue', 'Revenue from selling goods or services', 'R', FALSE), -- Sales Revenue
    ('4100.001', 'Interest Income', 'Interest earned on investments', 'R', FALSE), -- Interest Income
    ('4200.001', 'Rental Income', 'Income from rental properties or assets', 'R', FALSE); -- Rental Income

-- Expenses (X)
INSERT INTO Account (AccountCode, AccountName, AccountDescription, AccountType, IsContra) VALUES
    ('5000.001', 'Cost of Goods Sold', 'Direct costs of goods sold or services provided', 'X', FALSE), -- Cost of Goods Sold
    ('5100.001', 'Salaries and Wages', 'Employee salaries and wages', 'X', FALSE), -- Salaries and Wages
    ('5200.001', 'Rent Expense', 'Rent for office or warehouse space', 'X', FALSE), -- Rent Expense
    ('5300.001', 'Utilities Expense', 'Electricity, water, and other utility costs', 'X', FALSE), -- Utilities Expense
    ('5400.001', 'Depreciation Expense', 'Allocation of the cost of tangible assets over time', 'X', FALSE); -- Depreciation Expense
