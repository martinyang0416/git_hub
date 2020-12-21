package usecase.people;

import exception.people.ItHasSameNameAlready;
import exception.people.ShouldCreatePersonException;
import entity.people.Person;
import entity.people.factory.PersonFactory;

import java.util.*;

public class PeopleManager implements PeopleManage{

    private final Map<UUID, Person> people_base;
    private final Map <String, String> password_base;
    private final List<UUID> id_base;
    private final Map <String, UUID> name_base;

    public PeopleManager(){
        people_base = new HashMap<>();
        password_base = new HashMap<>();
        id_base = new ArrayList<>();
        name_base = new HashMap<>();
    }
    
    /**
     * Login will return null if username is not exist or the password
     * cannot match, otherwise return the person relative to the username.
     * @param username the username provided
     * @param password the password provided
     * @return person or null
     */
    @Override
    public UUID login(String username, String password) {
        if (!name_base.containsKey(username)){return null;}
        // should be not equals
        if (!password_base.get(username).equals(password)){return null;}
        return people_base.get(name_base.get(username)).getID();
    }

    /**
     * Set the password of an account.
     * @param person the person
     * @param newPassword the new password
     * @throws ShouldCreatePersonException: throw ShouldCreatePersonException if the person is not
     * create
     */
    @Override
    public void setPassword(UUID person, String newPassword) throws ShouldCreatePersonException {
        if (!people_base.containsKey(person)){
            throw new ShouldCreatePersonException("You should create this Person at first.");}
        else{
            Person p = people_base.get(person);
            password_base.replace(p.getName(), newPassword);}
    }

    /**
     * Get the name according to given person.
     * @param person the person
     * @return name of the person
     */
    @Override
    public String getName(UUID person) {
        Person p = people_base.get(person);
        return p.getName();
    }

    /**
     * Set the new name of the given person.
     * @param person the person.
     * @param newName the name.
     * @throws ShouldCreatePersonException: throw ShouldCreatePersonException if the person is not
     *      * create
     */
    @Override
    public void setName(UUID person, String newName) throws ShouldCreatePersonException {
        if (!people_base.containsKey(person)){
            throw new ShouldCreatePersonException("You should create this Person at first.");}
        else{Person p = people_base.get(person);p.setName(newName);}
    }

    /**
     * Return this person can  attend or not.
     * @param person the person.
     * @return true if this person can attend
     */
    @Override
    public boolean canAttend(UUID person) {
        Person p = people_base.get(person);
        return p.canAttend();
    }

    /**
     * Return this person can be speak or not.
     * @param person the person.
     * @return true if this person can speak
     */
    @Override
    public boolean canSpeak(UUID person) {
        Person p = people_base.get(person);
        return p.canSpeak();
    }

    /**
     * Return this person can be organize or not.
     * @param person the person.
     * @return true if this person can organize
     */
    @Override
    public boolean canOrganize(UUID person) {
        Person p = people_base.get(person);
        return p.canOrganize();
    }

    /**
     * Get all people's id and return them in a list.
     * @return all people's id in a list
     */
    @Override
    public List<UUID> getAllPeople() {
        return id_base;
    }

    /**
     * Create a person by type, name and  password, it might raise ItHasSameNameAlready
     * if name is already existed.
     * @param type the type
     * @param name the name
     * @param password the password
     * @return create a person if the creation is valid
     * @throws ItHasSameNameAlready if the name is already existed in system
     */
    @Override
    public UUID create (String type, String name, String password) throws ItHasSameNameAlready {
        if (hasSameUsername(name)){
            throw new ItHasSameNameAlready("The name is existed already, please change another.");}
        else{
        PersonFactory factory = new PersonFactory();
        Person person = factory.create(type, name);
        UUID id = person.getID();
        people_base.put(id, person);
        password_base.put(name, password);
        id_base.add(id);
        name_base.put(name, id);
        return id;}
    }

    /**
     * Return whether the name is already exist in the base.
     * @param username  the username
     * @return true if the name is existed
     */
    @Override
    public boolean hasSameUsername(String username) {
        return name_base.containsKey(username);
    }

}
