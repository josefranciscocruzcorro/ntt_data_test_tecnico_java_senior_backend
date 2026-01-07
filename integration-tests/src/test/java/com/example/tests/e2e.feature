Feature: E2E Senior System Test

Background:
    * url 'http://localhost'

Scenario: Full Flow (Create Client -> Async Replica -> Create Account -> Check Report)

    # 1. Create Client (MS-Clientes :8081)
    Given url 'http://localhost:8081/clientes'
    And request { nombre: 'Juan Senior', genero: 'M', identificacion: '1234567890', direccion: 'Calle Falsa', telefono: '0999999', contrasena: '1234', estado: true }
    When method post
    Then status 201
    And match response.id == '#number'
    * def clientId = response.id
    * print 'Client Created with ID:', clientId

    # 2. WAIT for Async Kafka (Give it a second to replicate to MS-Cuentas)
    * def sleep = function(millis){ java.lang.Thread.sleep(millis) }
    * call sleep 3000

    # 3. Create Account (MS-Cuentas :8082) - Use ClientId
    Given url 'http://localhost:8082/cuentas'
    And request { numeroCuenta: '999999', tipoCuenta: 'Ahorros', saldoInicial: 1000, estado: true, clienteId: clientId }
    When method post
    Then status 201
    And match response.id == '#number'
    * def cuentaId = response.id

    # 4. Create Movement (Debit)
    Given url 'http://localhost:8082/movimientos'
    And request { cuenta: { id: cuentaId }, tipoMovimiento: 'Debito', valor: 100 }
    When method post
    Then status 201
    And match response.saldo == 900

    # 5. Generate Report (MS-Cuentas :8082)
    # Date logic for today
    * def now = function(){ return java.time.LocalDate.now().toString() }
    * def today = now()
    
    Given url 'http://localhost:8082/reportes'
    And param clientId = clientId
    And param startDate = today
    And param endDate = today
    When method get
    Then status 200
    And match response[0].cliente == 'Juan Senior'
    # 'Juan Senior' comes from the Replica table. If Kafka failed, it would be 'Unknown/External' or empty.
    And match response[0].saldoDisponible == 900
