CREATE TABLE IF NOT EXISTS monevi_student (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    nim VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS monevi_organization (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    abbreviation VARCHAR(255),
    PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS monevi_terms (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    organization_id VARCHAR(255) NOT NULL,
    student_id VARCHAR(255) NOT NULL,
    period_month INT NOT NULL,
    period_year INT NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (organization_id) REFERENCES monevi_organization (id),
    FOREIGN KEY (student_id) REFERENCES monevi_student (id)
)

CREATE TABLE IF NOT EXISTS monevi_region (
     id VARCHAR(255),
     mark_for_delete BOOLEAN NOT NULL,
     created_date TIMESTAMP NOT NULL,
     created_by VARCHAR(255) NOT NULL,
     updated_date TIMESTAMP NOT NULL,
     updated_by VARCHAR(255) NOT NULL,
     name VARCHAR(255) NOT NULL,
     PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS monevi_organization_region (
     id VARCHAR(255),
     mark_for_delete BOOLEAN NOT NULL,
     created_date TIMESTAMP NOT NULL,
     created_by VARCHAR(255) NOT NULL,
     updated_date TIMESTAMP NOT NULL,
     updated_by VARCHAR(255) NOT NULL,
     organization_id VARCHAR(255) NOT NULL,
     region_id VARCHAR(255) NOT NULL,
     PRIMARY KEY (id),
     FOREIGN KEY (organization_id) REFERENCES monevi_organization (id),
     FOREIGN KEY (region_id) REFERENCES monevi_region(id)
)

CREATE TABLE IF NOT EXISTS monevi_report (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    organization_region_id VARCHAR(255) NOT NULL,
    period_month INT NOT NULL,
    period_year INT NOT NULL,
    status VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY (organization_region_id) REFERENCES monevi_organization_region (id)
)

CREATE TABLE IF NOT EXISTS monevi_program (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    budget DECIMAL NOT NULL,
    subsidy DECIMAL NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS monevi_transaction (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    report_id VARCHAR(255) NOT NULL,
    program_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    amount DECIMAL NOT NULL,
    entry_position VARCHAR(10) NOT NULL,
    type VARCHAR(255) NOT NULL,
    general_ledger_account VARCHAR(255) NOT NULL,
    description TEXT,
    proof BYTEA NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (report_id) REFERENCES monevi_report (id),
    FOREIGN KEY (program_id) REFERENCES monevi_program (id)
)

CREATE TABLE IF NOT EXISTS monevi_supervisor (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
)

CREATE UNIQUE INDEX monevi_student_nim_key ON monevi_student (nim) WHERE mark_for_delete IS FALSE;
CREATE UNIQUE INDEX monevi_student_email ON monevi_student (email) WHERE mark_for_delete IS FALSE;
CREATE UNIQUE INDEX monevi_organization_name_key ON monevi_organization ("name") WHERE mark_for_delete IS FALSE;
CREATE UNIQUE INDEX monevi_supervisor_email_key ON monevi_supervisor (email) WHERE mark_for_delete IS FALSE;
CREATE UNIQUE INDEX monevi_region_name_key ON monevi_region (name) WHERE mark_for_delete IS FALSE;

ALTER TABLE monevi_student ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_student ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_organization ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_organization ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_terms ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_terms ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_region ALTER created_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_region ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_organization_region ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_organization_region ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_report ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_report ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_program ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_program ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_program ALTER start_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_program ALTER end_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_transaction ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_transaction ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_transaction ALTER transaction_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_supervisor ALTER created_date TYPE TIMESTAMPTZ USING created_date AT TIME ZONE 'UTC';
ALTER TABLE monevi_supervisor ALTER updated_date TYPE TIMESTAMPTZ USING updated_date AT TIME ZONE 'UTC';

ALTER TABLE monevi_terms ADD COLUMN locked_account BOOLEAN NOT NULL;

ALTER TABLE monevi_terms DROP COLUMN IF EXISTS organization_id CASCADE;
ALTER TABLE monevi_terms ADD COLUMN organization_region_id VARCHAR(255) NOT NULL;
ALTER TABLE monevi_terms ADD CONSTRAINT monevi_terms_organization_region_id_fkey FOREIGN KEY (organization_region_id) REFERENCES monevi_organization_region(id);

CREATE TABLE IF NOT EXISTS monevi_general_ledger_account (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    report_id VARCHAR(255) NOT NULL,
    total DECIMAL NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    FOREIGN KEY (report_id) REFERENCES monevi_report (id)
)

ALTER TABLE monevi_transaction RENAME COLUMN general_ledger_account TO general_ledger_account_id;
ALTER TABLE monevi_transaction ADD CONSTRAINT monevi_transaction_general_ledger_account_id_fkey FOREIGN KEY (general_ledger_account_id) REFERENCES monevi_general_ledger_account(id);
ALTER TABLE monevi_report ADD COLUMN comment TEXT;
ALTER TABLE monevi_report DROP COLUMN comment;

CREATE TABLE IF NOT EXISTS monevi_report_comment (
    id VARCHAR(255),
    mark_for_delete BOOLEAN NOT NULL,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    updated_by VARCHAR(255) NOT NULL,
    report_id VARCHAR(255) NOT NULL,
    content TEXT,
    commented_by VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (report_id) REFERENCES monevi_report (id)
)


ALTER TABLE monevi_report DROP COLUMN period_month;
ALTER TABLE monevi_report DROP COLUMN period_year;
ALTER TABLE monevi_report ADD COLUMN period_date TIMESTAMPTZ;

DROP TABLE monevi_supervisor;
ALTER TABLE monevi_student ALTER COLUMN nim DROP NOT NULL;
ALTER TABLE monevi_terms ALTER COLUMN student_id DROP NOT NULL;
ALTER TABLE monevi_terms RENAME COLUMN student_id TO user_account_id;
ALTER TABLE monevi_terms RENAME CONSTRAINT monevi_terms_student_id_fkey TO monevi_terms_user_account_id;
ALTER TABLE monevi_student ADD COLUMN role VARCHAR(255) NOT NULL;
ALTER TABLE monevi_student RENAME TO monevi_user_account;
ALTER TABLE monevi_user_account RENAME CONSTRAINT monevi_student_pkey TO monevi_user_account_pkey;
ALTER TABLE monevi_user_account RENAME COLUMN role TO type;
DROP TABLE monevi_terms;
ALTER TABLE monevi_user_account ADD COLUMN period_month INTEGER;
ALTER TABLE monevi_user_account ADD COLUMN period_year INTEGER;
ALTER TABLE monevi_user_account ADD COLUMN organization_region_id VARCHAR(255);
ALTER TABLE monevi_user_account ADD CONSTRAINT monevi_user_account_organization_region_id_fkey FOREIGN KEY (organization_region_id) REFERENCES monevi_organization_region (id);
ALTER TABLE monevi_user_account ADD COLUMN locked_account BOOLEAN NOT NULL;
ALTER TABLE monevi_user_account RENAME COLUMN type TO role;

ALTER TABLE monevi_program ADD COLUMN organization_region_id VARCHAR(255);
ALTER TABLE monevi_program ADD CONSTRAINT monevi_program_organization_region_id_fkey FOREIGN KEY (organization_region_id) REFERENCES monevi_organization_region (id);

ALTER TABLE monevi_transaction ALTER COLUMN report_id DROP NOT NULL;
ALTER TABLE monevi_transaction ALTER COLUMN general_ledger_account_id DROP NOT NULL;
ALTER TABLE monevi_general_ledger_account RENAME TO monevi_report_general_ledger_account;
ALTER TABLE monevi_transaction DROP COLUMN report_id;
ALTER TABLE monevi_transaction DROP COLUMN general_ledger_account_id;
ALTER TABLE monevi_transaction ADD COLUMN general_ledger_account_type VARCHAR(255) NOT NULL;
ALTER TABLE monevi_transaction ADD COLUMN report_id VARCHAR(255);
ALTER TABLE monevi_transaction ADD CONSTRAINT monevi_transaction_report_id_fkey FOREIGN KEY (report_id) REFERENCES monevi_report (id);