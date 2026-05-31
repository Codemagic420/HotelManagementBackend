import http from 'k6/http';
import { check, sleep, group } from 'k6';
import { Rate, Trend } from 'k6/metrics';

/**
 * k6 Performance Testing Script - Hotel Management System
 * Tests: Load, Stress, Spike, Endurance testing
 */

// Custom metrics
const errorRate = new Rate('errors');
const apiDuration = new Trend('api_duration');

// Test configuration
export const options = {
  stages: [
    // Warm up - ramp up to 10 users over 30s
    { duration: '30s', target: 10 },
    
    // Load test - stay at 10 users for 2 minutes
    { duration: '2m', target: 10 },
    
    // Stress test - ramp up to 50 users over 1 minute
    { duration: '1m', target: 50 },
    
    // Spike test - jump to 100 users
    { duration: '30s', target: 100 },
    
    // Recovery - ramp down to 10 users
    { duration: '1m', target: 10 },
    
    // Cool down
    { duration: '30s', target: 0 }
  ],
  
  // Thresholds - define when test should fail
  thresholds: {
    'http_req_duration': ['p(95)<500', 'p(99)<1000'], // 95% requests < 500ms
    'http_req_failed': ['rate<0.1'],                   // Error rate < 10%
    'errors': ['rate<0.1']                             // Custom error rate < 10%
  }
};

const BASE_URL = 'http://localhost:8080';
const THINK_TIME = 1000; // 1 second between requests

export default function () {
  // Login
  let token = login();
  
  if (!token) {
    console.error('Failed to obtain JWT token');
    return;
  }

  // Test groups
  guestManagementTests(token);
  sleep(0.5);
  
  roomManagementTests(token);
  sleep(0.5);
  
  reservationWorkflowTests(token);
  sleep(0.5);
  
  billManagementTests(token);
  sleep(0.5);
}

/**
 * Login and obtain JWT token
 */
function login() {
  const loginPayload = JSON.stringify({
    username: 'admin',
    password: 'admin123'
  });

  const params = {
    headers: {
      'Content-Type': 'application/json'
    }
  };

  const response = http.post(`${BASE_URL}/api/auth/login`, loginPayload, params);
  
  const success = check(response, {
    'login status is 200': (r) => r.status === 200,
    'response has token': (r) => r.body.includes('token')
  });

  if (!success) {
    errorRate.add(1);
    return null;
  }

  try {
    return JSON.parse(response.body).token;
  } catch (e) {
    errorRate.add(1);
    return null;
  }
}

/**
 * Guest Management Tests
 */
function guestManagementTests(token) {
  group('Guest Management - GET all guests', () => {
    const params = {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    };

    const response = http.get(`${BASE_URL}/api/guests`, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 200': (r) => r.status === 200,
      'response is array': (r) => Array.isArray(JSON.parse(r.body)),
      'response time < 500ms': (r) => r.timings.duration < 500
    }) || errorRate.add(1);
  });

  sleep(THINK_TIME / 1000);

  group('Guest Management - GET single guest', () => {
    const params = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };

    const response = http.get(`${BASE_URL}/api/guests/1`, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 200': (r) => r.status === 200,
      'has guestId': (r) => r.body.includes('guestId'),
      'response time < 300ms': (r) => r.timings.duration < 300
    }) || errorRate.add(1);
  });

  sleep(THINK_TIME / 1000);

  group('Guest Management - CREATE guest', () => {
    const timestamp = new Date().getTime();
    const payload = JSON.stringify({
      firstName: `LoadTest`,
      lastName: `User${timestamp}`,
      email: `loadtest${timestamp}@hotel.com`,
      phone: '+4540123456'
    });

    const params = {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    };

    const response = http.post(`${BASE_URL}/api/guests`, payload, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 201': (r) => r.status === 201,
      'has guestId': (r) => r.body.includes('guestId'),
      'response time < 800ms': (r) => r.timings.duration < 800
    }) || errorRate.add(1);
  });
}

/**
 * Room Management Tests
 */
function roomManagementTests(token) {
  group('Room Management - GET all rooms', () => {
    const params = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };

    const response = http.get(`${BASE_URL}/api/rooms`, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 200': (r) => r.status === 200,
      'response is array': (r) => Array.isArray(JSON.parse(r.body)),
      'response time < 500ms': (r) => r.timings.duration < 500
    }) || errorRate.add(1);
  });

  sleep(THINK_TIME / 1000);

  group('Room Management - GET room by ID', () => {
    const params = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };

    // Test multiple room IDs for variety
    const roomId = Math.floor(Math.random() * 110) + 1;
    const response = http.get(`${BASE_URL}/api/rooms/${roomId}`, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 200 or 404': (r) => [200, 404].includes(r.status),
      'response time < 300ms': (r) => r.timings.duration < 300
    }) || errorRate.add(1);
  });
}

/**
 * Reservation Workflow Tests
 */
function reservationWorkflowTests(token) {
  group('Reservation - GET all reservations', () => {
    const params = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };

    const response = http.get(`${BASE_URL}/api/reservations`, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 200': (r) => r.status === 200,
      'response is array': (r) => Array.isArray(JSON.parse(r.body)),
      'response time < 500ms': (r) => r.timings.duration < 500
    }) || errorRate.add(1);
  });

  sleep(THINK_TIME / 1000);

  group('Reservation - CREATE reservation', () => {
    const futureDate = new Date();
    futureDate.setDate(futureDate.getDate() + 10);
    const checkInDate = futureDate.toISOString().split('T')[0];
    
    futureDate.setDate(futureDate.getDate() + 5);
    const checkOutDate = futureDate.toISOString().split('T')[0];

    const payload = JSON.stringify({
      guestId: Math.floor(Math.random() * 150) + 1,
      roomId: Math.floor(Math.random() * 110) + 1,
      checkInDate: checkInDate,
      checkOutDate: checkOutDate,
      numberOfGuests: Math.floor(Math.random() * 4) + 1,
      specialRequests: 'No requests'
    });

    const params = {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    };

    const response = http.post(`${BASE_URL}/api/reservations`, payload, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 201 or 400': (r) => [201, 400].includes(r.status),
      'response time < 800ms': (r) => r.timings.duration < 800
    }) || errorRate.add(1);
  });
}

/**
 * Bill Management Tests
 */
function billManagementTests(token) {
  group('Bill Management - GET all bills', () => {
    const params = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };

    const response = http.get(`${BASE_URL}/api/bills`, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 200': (r) => r.status === 200,
      'response is array': (r) => Array.isArray(JSON.parse(r.body)),
      'response time < 500ms': (r) => r.timings.duration < 500
    }) || errorRate.add(1);
  });

  sleep(THINK_TIME / 1000);

  group('Bill Management - GET bill by ID', () => {
    const params = {
      headers: {
        Authorization: `Bearer ${token}`
      }
    };

    const billId = Math.floor(Math.random() * 120) + 1;
    const response = http.get(`${BASE_URL}/api/bills/${billId}`, params);
    apiDuration.add(response.timings.duration);

    check(response, {
      'status is 200 or 404': (r) => [200, 404].includes(r.status),
      'response time < 300ms': (r) => r.timings.duration < 300
    }) || errorRate.add(1);
  });
}

/**
 * Handles summary and trend data
 */
export function handleSummary(data) {
  return {
    'performance-test-results.json': JSON.stringify(data, null, 2)
  };
}
