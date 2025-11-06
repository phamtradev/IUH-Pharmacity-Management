-- Kiem tra tat ca constraints cua bang dontrahang
USE IUHPharmacityManagement;

SELECT 
    OBJECT_NAME(object_id) AS ConstraintName,
    OBJECT_NAME(parent_object_id) AS TableName,
    definition,
    type_desc
FROM sys.check_constraints
WHERE parent_object_id = OBJECT_ID('dbo.dontrahang');

-- Kiem tra tat ca constraints (ke ca foreign key, primary key)
SELECT 
    OBJECT_NAME(object_id) AS ConstraintName,
    type_desc
FROM sys.objects
WHERE parent_object_id = OBJECT_ID('dbo.dontrahang')
    AND type IN ('C', 'D', 'F', 'PK', 'UQ');

