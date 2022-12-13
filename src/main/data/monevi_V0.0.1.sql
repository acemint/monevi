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
