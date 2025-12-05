package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.studentmanagement.entities.Department;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DepartmentServiceTests {

    @Autowired
    DepartmentService departmentService;

    @Test
    void testSaveAndGetDepartment() {
        Department dep = new Department();
        dep.setName("Test Department");

        // Save department
        Department saved = departmentService.saveDepartment(dep);
        assertNotNull(saved.getId());

        // Get by ID
        Department fetched = departmentService.getDepartmentById(saved.getId());
        assertEquals("Test Department", fetched.getName());

        // Get all departments
        List<Department> allDeps = departmentService.getAllDepartments();
        assertTrue(allDeps.size() >= 1);

        // Delete
        departmentService.deleteDepartment(saved.getId());
        assertThrows(Exception.class, () -> departmentService.getDepartmentById(saved.getId()));
    }
}
